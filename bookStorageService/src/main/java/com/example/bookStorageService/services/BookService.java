package com.example.bookStorageService.services;

import com.example.bookStorageService.exceptions.BookValidationException;
import com.example.bookStorageService.mappers.BookMapper;
import com.example.bookStorageService.mappers.BookWithoutIdMapper;
import com.example.bookStorageService.models.Book;
import com.example.bookStorageService.models.BookDTO;
import com.example.bookStorageService.models.BookWithoutIdDTO;
import com.example.bookStorageService.repository.BookRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class BookService {
    private final BookRepository bookRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookMapper bookMapper = BookMapper.INSTANCE;
    private final BookWithoutIdMapper bookWithoutIdMapper = BookWithoutIdMapper.INSTANCE;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public BookService(WebClient.Builder webClientBuilder, BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.webClientBuilder = webClientBuilder;
    }

    public BookWithoutIdDTO findBookById(Long id) throws EntityNotFoundException {
        return bookRepository.findById(id)
                .map(bookWithoutIdMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Book  (Id: " + id + ") not found"));
    }

    public BookDTO findBookByIsbn(String isbn) throws EntityNotFoundException {
        return bookRepository.findByIsbn(isbn)
                .map(bookMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Book (Isbn: " + isbn + ") not found"));
    }

    public List<BookDTO> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookDTO updateBook(Long id, BookDTO updatedBookDTO) throws EntityNotFoundException {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with ID " + id + " not found"));
        existingBook.setTitle(updatedBookDTO.getTitle());
        existingBook.setAuthor(updatedBookDTO.getAuthor());
        existingBook.setIsbn(updatedBookDTO.getIsbn());
        existingBook.setGenre(updatedBookDTO.getGenre());
        existingBook.setDescription(updatedBookDTO.getDescription());
        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.toDTO(updatedBook);
    }

    private void sendDeleteBookAsyncHTTPRequestToExternalService(Long bookId, String token) {
        String externalServiceUrl = "http://localhost:8080/api/library/delete/" + bookId;
        webClientBuilder.build()
                .delete()
                .uri(externalServiceUrl)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(response -> logger.info("Book ID {} successfully deleted", bookId))
                .doOnError(error -> logger.error("Failed to delete Book ID {} in external service: {}", bookId, error.getMessage()))
                .subscribe();
    }

    @Transactional
    public void deleteById(Long id, String token) throws EntityNotFoundException {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with ID " + id + " not found");
        }
        bookRepository.deleteById(id);
        sendDeleteBookAsyncHTTPRequestToExternalService(id, token);
    }

    private void sendAddBookAsyncHTTPRequestToExternalService(Long bookId, String token) {
        String externalServiceUrl = "http://localhost:8080/api/library/add/" + bookId;
        webClientBuilder.build()
                .post()
                .uri(externalServiceUrl)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(response -> logger.info("Successfully processed with Book ID {} ", bookId))
                .doOnError(error -> logger.error("Failed to send Book ID {} to external service: {}", bookId, error.getMessage()))
                .subscribe();
    }

    @Transactional
    public BookWithoutIdDTO saveBook(BookWithoutIdDTO bookWithoutIdDTO, String token) throws BookValidationException, EntityExistsException {
        if (bookRepository.findByIsbn(bookWithoutIdDTO.getIsbn()).isPresent()) {
            throw new EntityExistsException("Book with the same ISBN already exists");
        }
        Book book = bookWithoutIdMapper.toEntity(bookWithoutIdDTO);
        Book savedBook = bookRepository.save(book);
        sendAddBookAsyncHTTPRequestToExternalService(savedBook.getId(), token);
        return bookWithoutIdMapper.toDTO(savedBook);
    }
}
