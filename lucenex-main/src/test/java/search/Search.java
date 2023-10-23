package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Search {

	public static void main(String[] args) throws IOException, ParseException {

		Directory directory = FSDirectory.open(Paths.get("target/index_HM2_IDD")); // Define where to save Lucene index
		IndexReader reader = DirectoryReader.open(directory); // obtain read access to the index 
		IndexSearcher searcher = new IndexSearcher(reader);

		String field = "";
		while(true) {
			while(true) {
				System.out.print("Vuoi fare una query su nome o contenuto? ");
				BufferedReader buffReader1 = new BufferedReader(new InputStreamReader(System.in));
				field = buffReader1.readLine().toLowerCase();
				if(field.equals("nome") || field.equals("contenuto")) {
					break;
				}
			}

			System.out.print("Inserisci una query: ");
			BufferedReader buffReader2 = new BufferedReader(new InputStreamReader(System.in));
			String queryString = buffReader2.readLine();

			String[] parole = queryString.split(" ");

			PhraseQuery.Builder builder = new PhraseQuery.Builder();
			for(String parola: parole) {
				builder.add(new Term(field,parola));
			}
			PhraseQuery phraseQuery = builder.build();


			TopDocs hits = searcher.search(phraseQuery, 5); //search for the top 1 document that match the query 
			for (int i = 0; i < hits.scoreDocs.length; i++) {
				ScoreDoc scoreDoc = hits.scoreDocs[i];
				int docId = scoreDoc.doc;
				Document doc = searcher.doc(scoreDoc.doc); //fetch returned document 
				System.out.println("L'indice ha ritornato il seguente documento: " + searcher.doc(docId).get("nome"));
			}

			if(hits.scoreDocs.length == 0) {
				System.out.println("Mi dispiace, non ci sono documenti che soddisfano la tua richiesta");
			}

			System.out.print("Scrivi exit se non vuoi fare altre query, digita qualunque cosa per continuare: ");
			BufferedReader buffReader3 = new BufferedReader(new InputStreamReader(System.in));
			field = buffReader3.readLine().toLowerCase();
			if(field.equals("exit")){
				break;
			}

		}
	}

}
