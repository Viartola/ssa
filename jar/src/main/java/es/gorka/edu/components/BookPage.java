package es.gorka.edu.components;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.gorka.edu.dto.BookDTO;
import es.gorka.edu.service.BookService;

public class BookPage extends WebPage {
	
	@SpringBean
	BookService bookService;
	
	@SpringBean
	BookDTO bookDto;
	
	public BookPage() {
		add(new Label("title", getString("title")));
		Form<BookDTO> form = new Form<BookDTO>("formAddNewBook", new CompoundPropertyModel<BookDTO>(bookDto)){
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				super.onSubmit();
				boolean isInserted = bookService.insertNewBook(getModelObject());
				FeedbackMessage message;
				if(isInserted){
					message = new FeedbackMessage(this, "Libro insertado", FeedbackMessage.INFO);
				} else {
					message = new FeedbackMessage(this, "No se pudo insertar", FeedbackMessage.INFO);
				}
				getFeedbackMessages().add(message);
			}
		};
		form.add(new Label("nameBookLabel", getString("book.name")));
		form.add(new Label("ISBNLabel", getString("ISBN")));
		form.add(new Label("nameAuthorLabel", getString("author.name")));
		form.add(new RequiredTextField("nameBook"));
		form.add(new RequiredTextField("ISBN"));
		form.add(new RequiredTextField("nameAuthor"));
		FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);
		add(form);
		add(new BookmarkablePageLink<String>("libraryLink", LibraryPage.class));
	}
}
