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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class embeddedClausesInNPs {

    public static void main(String... args) throws ParserConfigurationException, SAXException, IOException {
        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,sentiment,kbp,quote");
        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
        props.setProperty("coref.algorithm", "neural");
        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        File[] files = new File("C:/Users/Administrator/Documents/NetBeansProjects/java_xml_reader/all_xml").listFiles();
        analyzeFiles(files, pipeline);
    }

    public static void analyzeFiles(File[] files, StanfordCoreNLP pipeline) throws ParserConfigurationException, SAXException, IOException {
      // ABSTRACT
      /*  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        for (File file : files) {
            Document doc = builder.parse(file);
            NodeList abstractList;
            Element abstractEl = (Element) doc.getElementsByTagName("bodyText").item(0);
            String abstracttext = abstractEl.getTextContent(); //.replaceAll("[^a-zA-Z ]", " ");
            //System.out.println("My abstract text:\n " + abstracttext);
            // annnotate the document
            // create a document object
           // abstracttext = abstracttext.replaceAll("\n", "");
            
           StringBuilder sb = new StringBuilder();
           String[] lines = abstracttext.split("\n");
           for(String l : lines) {
               //System.out.println("aLine: " + l);
               if(l.endsWith("-")) {
                   sb.append(l.substring(0, l.length()-1));
               }
               else {
                   sb.append(l+ " ");
               }
           }
           abstracttext = sb.toString();
           
           //System.out.println(abstracttext);
           String small = abstracttext.toLowerCase();
           //System.out.println("Lower Case Text: " + small);
           
            CoreDocument document = new CoreDocument(small);

            pipeline.annotate(document); */
      // BODY SECTION
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();        
        for (File file : files) {
            ArrayList<String> bodyTextFractions = new ArrayList<>();
            ArrayList<String> abstractTextFractions = new ArrayList<>();
            
            //System.out.println("Analyzing file: " + file.getName());
            Document doc = builder.parse(file);
            
            
            NodeList bodyTextElements = doc.getElementsByTagName("bodyText");
            for (int n = 1; n < bodyTextElements.getLength(); n++) {
                
                Node nNode = bodyTextElements.item(n);
                String nNodeText = nNode.getTextContent();
                StringBuilder sb = new StringBuilder();
                String[] lines = nNodeText.split("\n");
                for (String l : lines) {
                    //System.out.println("aLine: " + l);
                    if (l.endsWith("-")) {
                        sb.append(l.substring(0, l.length() - 1));

                    } else {
                        sb.append(l + " ");
                    }

                }
                nNodeText = sb.toString();
                if (n > 0) {
                    bodyTextFractions.add(nNodeText);
                }
                if (n == 0) {
                    abstractTextFractions.add(nNodeText);
                }

            }

            
            StringBuilder bodyTextAggregated = new StringBuilder();
            for (String bodyTextContent : bodyTextFractions) {
                    //System.out.println("aLine: " + l);
                    if (bodyTextContent.endsWith("-")) {
                        bodyTextAggregated.append(bodyTextContent.substring(0, bodyTextContent.length() - 1));

                    } else {
                        bodyTextAggregated.append(bodyTextContent + " ");
                    }
            }
            String bodyTexContentCleaned = bodyTextAggregated.toString().trim();
            //System.out.println("body text cleaned: " + bodyTexContentCleaned);
            CoreDocument document = new CoreDocument(bodyTexContentCleaned);
            pipeline.annotate(document); 
            
             List<Tree> myNPs = new ArrayList<Tree>(); 
             List<Tree> embeddedCl = new ArrayList<Tree>(); 
             List<String> myPronouns = new ArrayList<String>();
             int tokensOfDoc = document.tokens().size();
            
            List<CoreSentence> sentencesOfDoc;
            sentencesOfDoc = document.sentences();
            int sentenceNum = document.sentences().size();
            for (int i = 0; i < sentenceNum; i++){
                CoreSentence sentence = document.sentences().get(i);
                List<String> posTags = sentence.posTags();
                int posNum = posTags.size();
                for (int j=0; j<posNum; j++){
                    String aPOS = posTags.get(j);
                    if (aPOS.startsWith("PRP")){
                        myPronouns.add(aPOS);
                    }
                
            }
               Tree constituencyParse = sentence.constituencyParse();
               //System.out.println("Example: constituency parse");
               //System.out.println(constituencyParse);
               //System.out.println();
               for (Tree subtree : constituencyParse) { 
                   if (subtree.label().value().equals("NP")) { 
                       myNPs.add(subtree);
                       for(Tree s : subtree) {
                           if (s.label().value().equals("SBAR")){
                               embeddedCl.add(s);
                           }
                       } 
                       
    }
               
} 
  }
            int countNPs = myNPs.size();
            int myPronounsNum = myPronouns.size();
            int withoutPronouns = countNPs - myPronounsNum;
            int embeddedClNum = embeddedCl.size();
            //System.out.println(myNPs);
            //System.out.println("Embedded Clauses: " + embeddedCl);
            double meanClModifiers = (double) embeddedClNum/withoutPronouns;
            meanClModifiers = Math.round(meanClModifiers*100.0)/100.0;  
            System.out.println("Analysing file:" + file.getName());
            // Printing the mean number of embedded finite clauses as modifiers in NPs
            System.out.println(meanClModifiers);
           
           
            
        }

    }
}