package es.gorka.edu.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.gorka.edu.dto.BookDTO;
import es.gorka.edu.service.BookService;

public class ListBookPage extends WebPage {
	private static final long serialVersionUID = -1935854748907274886L;

	@SpringBean
	BookService bookService;
	
	@SpringBean
	BookDTO bookDto;

	private static final Logger logger = LogManager.getLogger(ListBookPage.class.getName());

	private String title = null;
	
	private String isbn = null;
	
	private String author = null;

	private List<BookDTO> listBook = Collections.emptyList();

	public ListBookPage(PageParameters parameters) {
		title = parameters.get("title").toString();
		isbn = parameters.get("isbn").toString();
		author = parameters.get("author").toString();
		logger.debug("Cargando la pagina con el parametro Titulo: "+ title +" ISBN: "+ isbn +" Autor: "+ author);
		initComponents();
	}

	public ListBookPage() {
		initComponents();
	}

	private void initComponents() {
		add(new Label("title", "Listar Libros"));
		addFormBook();
		addFeedBackPanel();
		addListBookView();
		add(new BookmarkablePageLink<String>("libraryLink", LibraryPage.class));
	}

	private void addFormBook() {
		Form<BookDTO> form = new Form<BookDTO>("formListBook", new CompoundPropertyModel<BookDTO>(bookDto)) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				super.onSubmit();
				listBook.clear();
				PageParameters pageParameters = new PageParameters();
				if(getModelObject().getNameBook() != null)
					pageParameters.add("title", getModelObject().getNameBook());
				if(getModelObject().getISBN() != null)
					pageParameters.add("isbn", getModelObject().getISBN());
				if(getModelObject().getNameAuthor() != null)
					pageParameters.add("author", getModelObject().getNameAuthor());
				setResponsePage(ListBookPage.class, pageParameters);
			}
		};
		form.add(new Label("nameBookLabel", getString("book.name")));
		form.add(new Label("ISBNLabel", getString("ISBN")));
		form.add(new Label("nameAuthorLabel", getString("author.name")));
		form.add(new TextField<String>("nameBook"));
		form.add(new TextField<String>("ISBN"));
		form.add(new TextField<String>("nameAuthor"));
		add(form);
	}

	private void addFeedBackPanel() {
		FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackMessage");
		add(feedbackPanel);
	}

	private void addListBookView() {
		BookDTO book = new BookDTO();
		if(title != null)
			book.setNameBook(title);
		if(isbn != null)
			book.setISBN(isbn);
		if(author != null)
			book.setNameAuthor(author);
		listBook = bookService.findBooks(book);
		ListView<BookDTO> listview = new ListView<BookDTO>("book-group", listBook) {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<BookDTO> item) {
				BookDTO book = item.getModelObject();
				item.add(new Label("title", book.getNameBook()));
				item.add(new Label("isbn", book.getISBN()));
				item.add(new Label("author", book.getNameAuthor()));
			}
		};
		add(listview);
	}

}
