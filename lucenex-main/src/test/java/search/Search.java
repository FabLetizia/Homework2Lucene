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
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Search {

	public static void main(String[] args) throws IOException, ParseException {

		Directory directory = FSDirectory.open(Paths.get("target/index_HM2_IDD")); 
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

			// Crea un oggetto BooleanQuery
			BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();

			for(String parola: parole) {
			    // Crea una query di termine per ogni parola
				Query termQuery = new TermQuery(new Term(field, parola));
			    // Aggiungi la query di termine alla BooleanQuery con un operatore OR
				booleanQueryBuilder.add(termQuery, BooleanClause.Occur.SHOULD);
			}
			// Costruisci la BooleanQuery finale
			BooleanQuery booleanQuery = booleanQueryBuilder.build();

			TopDocs hits = searcher.search(booleanQuery, 8); //search for the top 8 documents that match the query 
			for (int i = 0; i < hits.scoreDocs.length; i++) {
				ScoreDoc scoreDoc = hits.scoreDocs[i];
				int docId = scoreDoc.doc;
				Document doc = searcher.doc(scoreDoc.doc); //fetch returned document 
				System.out.println("L'indice ha ritornato il seguente documento: " + searcher.doc(docId).get("nome"));
			}

			if(hits.scoreDocs.length == 0) {
				System.out.println("Mi dispiace, non ci sono documenti che soddisfano la tua richiesta");
			}

			System.out.print("Scrivi exit se non vuoi fare altre query, digita qualunque altra cosa per continuare: ");
			BufferedReader buffReader3 = new BufferedReader(new InputStreamReader(System.in));
			field = buffReader3.readLine().toLowerCase();
			if(field.equals("exit")){
				break;
			}

		}
	}

}
