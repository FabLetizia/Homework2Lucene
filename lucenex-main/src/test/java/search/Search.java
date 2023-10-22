package search;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Search {

	public static void main(String[] args) throws IOException, ParseException {

		Directory directory = FSDirectory.open(Paths.get("/Users/fabio/Desktop/index_HM2_IDD")); // Define where to save Lucene index
		IndexReader reader = DirectoryReader.open(directory); // obtain read access to the index 
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser("nome", new WhitespaceAnalyzer());

		Query query = parser.parse("football.txt"); //define the query
		System.out.println("Hai inserito la seguente query: " + query);
		
		TopDocs hits = searcher.search(query, 2); //search for the top 1 document that match the query 
		for (int i = 0; i < hits.scoreDocs.length; i++) {
			ScoreDoc scoreDoc = hits.scoreDocs[i];
			int docId = scoreDoc.doc;
			Document doc = searcher.doc(scoreDoc.doc); //fetch returned document 
	        System.out.println("L'indice ha ritornato il seguente documento: " + searcher.doc(docId).get("nome"));
		}
        
		
	}

}
