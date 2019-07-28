/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaStanfordCoreNLP;

/**
 *
 * @author Administrator
 */
import static javaStanfordCoreNLP.Coresentence.aSentence;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import static javafx.scene.input.KeyCode.Z;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class countNP {

    public static void main(String... args) throws ParserConfigurationException, SAXException, IOException {
        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,sentiment,kbp,quote");
        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
        props.setProperty("coref.algorithm", "neural");
        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        File[] files = new File("C:/Users/Administrator/Documents/NetBeansProjects/java_xml_reader/all_Ks/test").listFiles();
        analyzeFiles(files, pipeline);
    }

    public static void analyzeFiles(File[] files, StanfordCoreNLP pipeline) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        for (File file : files) {
            Document doc = builder.parse(file);
            NodeList abstractList;
            Element abstractEl = (Element) doc.getElementsByTagName("bodyText").item(0);
            String abstracttext = abstractEl.getTextContent(); //.replaceAll("[^a-zA-Z ]", " ");
            StringBuilder sb = new StringBuilder();
            String[] lines = abstracttext.split("\n");
            for (String l : lines) {
                //System.out.println("aLine: " + l);
                if (l.endsWith("-")) {
                    sb.append(l.substring(0, l.length() - 1));
                } else {
                    sb.append(l + " ");
                }
            }
            abstracttext = sb.toString();
            CoreDocument document = new CoreDocument(abstracttext);
            pipeline.annotate(document);
            List<Tree> myNPs = new ArrayList<Tree>();
            List<CoreSentence> sentencesOfDoc;
            sentencesOfDoc = document.sentences();
            int sentenceNum = document.sentences().size();
            for (int i = 0; i < sentenceNum; i++) {
                CoreSentence sentence = document.sentences().get(i);
                // Tree class with which I can parse the constituents 
                Tree constituencyParse = sentence.constituencyParse();
                //System.out.println("Example: constituency parse");
                //System.out.println(constituencyParse);
                //System.out.println();

                //Iterate through the tree nodes and get the subtree whose value is an NP. Store the extracted NPs in a list
                
                for (Tree subtree : constituencyParse) {
                    if (subtree.label().value().equals("NP")) {
                        myNPs.add(subtree);
                    }

                }

                

            }
                int countNPs = myNPs.size(); 
                System.out.println(countNPs);
                System.out.println("Analysing file: " + file.getName());
                
                double meanNP = (double) countNPs/sentenceNum;
                meanNP = Math.round(meanNP*100.0)/100.0;
                //Printing out the mean number of NP occurrences per number of sentences
                System.out.println(meanNP);
        }

    }
}