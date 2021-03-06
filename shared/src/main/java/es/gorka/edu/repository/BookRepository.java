package es.gorka.edu.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gorka.edu.assembler.Assembler;
import es.gorka.edu.connection.AbstractConnectionManager;
import es.gorka.edu.dto.BookDTO;
import es.gorka.edu.model.Author;
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
		} finally {
			conManager.close(preparedStatement);
			conManager.close(connection);
		}
	}

	public List<BookDTO> findBooks(BookDTO bookDto) {
		ArrayList<BookDTO> list = new ArrayList<BookDTO>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = conManager.open();
			preparedStatement = connection.prepareStatement("SELECT * FROM BOOK WHERE nameBook LIKE ? OR ISBN LIKE ? OR nameAuthor LIKE ?");
			preparedStatement.setString(1, "%" + bookDto.getNameBook() + "%");
			preparedStatement.setString(2, "%" + bookDto.getISBN() + "%");
			preparedStatement.setString(3, "%" + bookDto.getNameAuthor() + "%");
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Book book = new Book();
				book.setNameBook(resultSet.getString(1));
				book.setISBN(resultSet.getString(2));
				book.setNameAuthor(resultSet.getString(3));
				list.add(asesembler.toDto(new BookDTO(), book));
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			conManager.close(resultSet);
			conManager.close(preparedStatement);
			conManager.close(connection);
		}

		return list;
	}
}
