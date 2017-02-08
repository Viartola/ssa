package es.gorka.edu.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gorka.edu.assembler.Assembler;
import es.gorka.edu.connection.AbstractConnectionManager;
import es.gorka.edu.dto.BookDTO;
import es.gorka.edu.model.Book;

@Service
public class BookRepository {
	
	private static final Logger logger = LogManager.getLogger(BookRepository.class.getName());
	
	@Autowired
	private AbstractConnectionManager conManager;
	
	@Autowired
	private Assembler<BookDTO, Book> asesembler;

	public void insertNewBook(BookDTO bookDto) {
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Book book = new Book();
		asesembler.toEntity(bookDto, book);
		try {
			connection = conManager.open();
			preparedStatement = connection.prepareStatement("INSERT INTO BOOK(nameBook, ISBN, nameAuthor) " + "VALUES (?, ?, ?)");
			preparedStatement.setString(1, book.getNameBook());
			preparedStatement.setString(2, book.getISBN());
			preparedStatement.setString(3, book.getNameAuthor());
			preparedStatement.executeUpdate();
			conManager.close(preparedStatement);

		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}
}