package es.gorka.edu.components;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class LibraryPage extends WebPage{
	
	private static final long serialVersionUID = 1L;

	public LibraryPage() {
		add(new Label("title", "Biblioteca"));
		add(new BookmarkablePageLink<String>("authorLink", AuthorPage.class));
		add(new BookmarkablePageLink<String>("authorListLink", ListAuthorPage.class));
		add(new BookmarkablePageLink<String>("bookLink", BookPage.class));
		add(new BookmarkablePageLink<String>("bookListLink", ListBookPage.class));
	}

}
