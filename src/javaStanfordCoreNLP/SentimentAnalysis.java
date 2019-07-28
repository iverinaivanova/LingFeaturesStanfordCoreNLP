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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SentimentAnalysis {

    public static void main(String... args) throws ParserConfigurationException, SAXException, IOException {
        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,sentiment,kbp,quote");
        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
        props.setProperty("coref.algorithm", "neural");
        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        File[] files = new File("C:/Users/Administrator/Documents/NetBeansProjects/java_xml_reader/all_Ss/Ss").listFiles();
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
           
            CoreDocument document = new CoreDocument(abstracttext);

            pipeline.annotate(document);

            List<CoreSentence> sentencesOfDoc;
            sentencesOfDoc = document.sentences();
            // Storing the sentence number in the sentence variable
            int sentences = document.sentences().size();
            //System.out.println(sentences);
            //int negSent = 0;
            //int posSent = 0;
            //int neutSent = 0;
            ArrayList<String> negative = new ArrayList<>();
            ArrayList<String> positive = new ArrayList<>();
            ArrayList<String> neutral = new ArrayList<>();

            //System.out.println("File: " + file);
            //Looping through each and every sentence and analysing the sentiment of each sentence
            for (int i = 0; i < sentences; i++) {
                CoreSentence sentence = document.sentences().get(i);
                String isSentiment = sentence.sentiment();
                //System.out.println("sentence: " + sentence);
                //System.out.println("sentence sentiment: " + isSentiment);

                if (null == isSentiment) {
                    //negSent ++;
                    negative.add(isSentiment);
                } else {
                    switch (isSentiment) {
                        case "Negative":
                            //negSent ++;
                            negative.add(isSentiment);
                            break;
                        case "Positive":
                            //posSent ++;
                            positive.add(isSentiment);
                            break;
                        case "Neutral":
                            //neutSent ++;
                            neutral.add(isSentiment);
                            // For example, occurrences of "Very Negative" sentiment
                            break;
                        default:
                            //negSent ++;
                            negative.add(isSentiment);
                            break;
                    }
                }

            }

            int allNeg = negative.size();
            int allPos = positive.size();
            int allNeut = neutral.size();

            System.out.println("Analysing file: " + file.getName());
            //System.out.println("allNeg: " + allNeg);
            //System.out.println("allPos: " + allPos);
            //System.out.println("allNeut: " + allNeut);
            double avgNegSent = (double) allNeg / sentences;
            avgNegSent = Math.round(avgNegSent*100.0)/100.0;
            // It calculates the average number of positive sentences per number of sentences
            double avgPosSent = (double) allPos / sentences;
            avgPosSent = Math.round(avgPosSent*100.0)/100.0;
            double avgNeutSent = (double) allNeut / sentences;
            avgNeutSent = Math.round(avgNeutSent*100.0)/100.0;
            System.out.println(avgNegSent);
            System.out.println(avgPosSent);
            System.out.println(avgNeutSent);

        }

    }

}
