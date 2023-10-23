package index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Index {

	public static void main(String[] args) throws IOException {

		/* analyzer per tokenizzare */
		Map<String, Analyzer> perFieldAnalyzers = new HashMap<>();

	    /* analyzer per il nome */
		perFieldAnalyzers.put("nome", new StandardAnalyzer());

	    /* analyzer per il contenuto */
		perFieldAnalyzers.put("contenuto", new WhitespaceAnalyzer());

		Analyzer analyzer = new PerFieldAnalyzerWrapper(new ItalianAnalyzer(), perFieldAnalyzers);

		Directory directory = FSDirectory.open(Paths.get("target/index_HM2_IDD")); // Define where to save Lucene index
		IndexWriterConfig config = new IndexWriterConfig(analyzer); // text processing semantics for
		IndexWriter writer = new IndexWriter(directory, config);   // Define an IndexWriter

        /* eclipse-workspace/lucene-main.zip_expanded/lucenex-main/target */
		/* Creo un ciclo per scorrere i file nella directory locale */
		File directoryPath = new File("target/file_HM2_IDD");
		File[] files = directoryPath.listFiles();

		/* Itera su ciascun file nella directory e aggiungilo all'indice utilizzando IndexWriter. 
		 * Assicurando di aprire il file, leggerne il contenuto e creare un oggetto Document che 
		 * rappresenti il documento da indicizzare */

		for (File file : files) {
			if (file.isFile() && file.getName().endsWith(".txt")) {
				Document document = new Document();

				/* campo "nome" del file */
				Field nameField = new StringField("nome", file.getName(), Field.Store.YES);
				document.add(nameField);

				/* campo "contenuto" del file */
				try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
					String line;
					while ((line = reader.readLine()) != null) {
						TextField contentField = new TextField("contenuto", line, Field.Store.NO);
						document.add(contentField);
					}
				}
				/* aggiunge il documento all'indice */
				writer.addDocument(document);
			}
		}

		writer.commit(); // persist changes to the disk
		writer.close();
	}
}


