package lingfeaturesstanfordcorenlp;

import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

/**
 *
 * @author Administrator
 */
public class AllFeaturesBody {

    public static void main(String... args) throws ParserConfigurationException, SAXException, IOException {
        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,sentiment,kbp,quote");
        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
        props.setProperty("coref.algorithm", "neural");
        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        File[] files = new File("../LinguisticFeaturesStanfordCoreNLP/all_Ks").listFiles();
        Arrays.sort(files);
        analyzeFiles(files, pipeline);
    }

    public static void analyzeFiles(File[] files, StanfordCoreNLP pipeline) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        for (File file : files) {
            Document doc = builder.parse(file);
            // ABSTRACT Extracting and formatting the abstract
             Element abstractEl = (Element) doc.getElementsByTagName("bodyText").item(0);
            String abstracttext = abstractEl.getTextContent(); //.replaceAll("[^a-zA-Z ]", " "); 
            StringBuilder sb = new StringBuilder();
            String[] lines = abstracttext.split("\n");
            for(String l : lines) {
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

            pipeline.annotate(document);
            
            // FULL TEXT. Extracting and formatting the full text
            
          /*  ArrayList<String> bodyTextFractions = new ArrayList<>();
            ArrayList<String> abstractTextFractions = new ArrayList<>();
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
            pipeline.annotate(document); */

            ArrayList<String> wordsInText = new ArrayList<>();
            ArrayList<String> nonWords = new ArrayList<>();
            List<String> myPronouns = new ArrayList<String>();
            List<CoreSentence> sentencesOfDoc;
            sentencesOfDoc = document.sentences();
            // Total number of sentences
            int sentCount = document.sentences().size();
            //Iterating over the tokens in a document and getting the words without the punctuation marks 
            int tokensOfDoc = document.tokens().size();
            for (int i = 0; i < tokensOfDoc; i++) {
                CoreLabel token = document.tokens().get(i);
                String aWord = token.word();
                if (aWord.matches("[A-Za-z]+")) {
                    wordsInText.add(aWord);

                } else {
                    nonWords.add(aWord);

                }
            } 
            // Printing file name
            System.out.println("Analysing file:" + file.getName());
            
            // Total number of tokens
            int wordsCount = wordsInText.size();
            
            // Getting the coreference chains per document
            Map<Integer, CorefChain> corefChains = document.corefChains();
            // Total number of corefchains
            int corefCount = corefChains.size();
            double corefChainsAvg = (double) corefCount / sentCount;
            corefChainsAvg = Math.round(corefChainsAvg * 100.0) / 100.0;
            // Printing the mean number of coreference chains
            System.out.println("The mean number of corefchains per number of sentences: ");
            System.out.println(corefChainsAvg);
            
            System.out.println("Number of sentences: ");
            System.out.println(sentCount);
            
            double sentLength = (double) wordsCount / sentCount;
            sentLength = Math.round(sentCompl * 100.0) / 100.0;
            System.out.println("Sentence length:");
            System.out.println(sentLength);
            
            // Create empty lists to which we will append the following constituents: NPs, embedded finite and non-finite clauses.
            List<Tree> myNPs = new ArrayList<Tree>();
            List<Tree> embeddedCl = new ArrayList<Tree>();
            List<Tree> embeddedVBN = new ArrayList<Tree>();
            List<Tree> embeddedTO = new ArrayList<Tree>();
            ArrayList<String> selfMent = new ArrayList<>();
            
            // Iterate over each token and 
            for (int i = 0; i < tokensOfDoc; i++) {
                CoreLabel token = document.tokens().get(i);
                String selfMentTokens = token.word();
                if (null == selfMentTokens) {

                } else {
                    switch (selfMentTokens) {
                        case "I":
                            selfMent.add(selfMentTokens);
                            break;
                        case "me":
                            selfMent.add(selfMentTokens);
                            break;
                        case "mine":
                            selfMent.add(selfMentTokens);
                            break;
                        case "we":
                            selfMent.add(selfMentTokens);
                            break;
                        case "our":
                            selfMent.add(selfMentTokens);
                            break;
                        case "us":
                            selfMent.add(selfMentTokens);
                            break;

                        default:
                            break;
                    }
                }
            }
            //Iterate over the sentences and get the total number of sentences
            sentencesOfDoc = document.sentences();
            for (int i = 0; i < sentCount; i++) {
                CoreSentence sentence = document.sentences().get(i);
                //Get the constituents for each sentence
                Tree constituencyParse = sentence.constituencyParse();
                for (Tree subtree : constituencyParse) {
                    // Iterate over the subtrees in the tree and look for the following labels of the constituents.
                    // If there is such a constituent, store it into the respective array.
                    if (subtree.label().value().equals("NP")) {
                        myNPs.add(subtree);
                        // Iterate over each NP structure and if the subtree label equals any of the labels below, append them to the respective array.
                        for (Tree s : subtree) {
                            // Look for embedded finite clauses
                            if (s.label().value().equals("SBAR")) {
                                embeddedCl.add(s);
                            //  Look for embedded past participle clauses 
                            } else if (s.label().value().equals("VBN")) {
                                embeddedVBN.add(s);
                            // Look for embedded TO-INF clauses
                            } else if (s.label().value().equals("TO")) {
                                embeddedTO.add(s);
                            }

                        }

                    }
                }

            }
            // Total number of NPs
            int countNPs = myNPs.size();
            
            // Total number of self-mentions
            int selfMentNum = selfMent.size();
            double meanSelfMentNum = (double) selfMentNum / countNPs;
            meanSelfMentNum = Math.round(meanSelfMentNum * 100.0) / 100.0;
            //Printing the mean number of self-mention
            System.out.println("Mean number of self mentions per document:");
            System.out.println(meanSelfMentNum);
            
            // Iterate over each token and get the words.
            for (int i = 0; i < tokensOfDoc; i++) {
                CoreLabel token = document.tokens().get(i);
                String aWord = token.word();
                if (aWord.matches("[A-Za-z]+")) {
                    wordsInText.add(aWord);

                } else {
                    nonWords.add(aWord);

                }
            }
           
            //Iterate over each sentence and for each sentence get the POS of the tokens
            for (int i = 0; i < sentCount; i++) {
                CoreSentence sentence = document.sentences().get(i);
                List<String> posTags = sentence.posTags();
                // Total number of POS tags
                int posTagsNum = posTags.size();
                // Iterate over each POS tag value and look for pronouns "PRP". If there are pronouns, append them to the "myPronouns" array.
                for (int n = 0; n < posTagsNum; n++) {
                    String posText = posTags.get(n);
                    if (posText.startsWith("PRP")) {
                        myPronouns.add(posText);
                    }
                }
            }
            
            double meanNP = (double) countNPs / wordsCount;
            meanNP = Math.round(meanNP * 100.0) / 100.0;
            //Printing out the mean number of NP occurrences per number of sentences
            System.out.println("The number of NPs per document normalized by number of tokens: ");
            System.out.println(meanNP);
            
            //Total number of pronouns
            int myPronounsNum = myPronouns.size();
            
            // Total number of NPs excluding the pronouns
            int withoutPronouns = countNPs - myPronounsNum;
            
            // Total number of finite clauses
            int embeddedClNum = embeddedCl.size();
            double meanClModifiers = (double) embeddedClNum / withoutPronouns;
            meanClModifiers = Math.round(meanClModifiers * 100.0) / 100.0;
            System.out.println("Mean Clause Modifiers:");
            System.out.println(meanClModifiers);
            
            // Total number of embedded past participle clauses
            int embeddedVBNNum = embeddedVBN.size();
            double meanVBNModifiers = (double) embeddedVBNNum / withoutPronouns;
            meanVBNModifiers = Math.round(meanVBNModifiers * 100.0) / 100.0;
            //Printing the mean number of past participle modifiers of noun heads 
            System.out.println("Mean non-finite clauses introduced by past participle:");
            System.out.println(meanVBNModifiers);
            
            // Total number of embedded TO-inf clauses
            int embeddedTONum = embeddedTO.size();
            double meanTOModifiers = (double) embeddedTONum / withoutPronouns;
            meanTOModifiers = Math.round(meanTOModifiers * 100.0) / 100.0;
            //Printing the mean number of non-finite clauses introduced by TO-inf. as modifiers of noun heads
            System.out.println("Mean non-finite clauses introduced by to-Inf.");
            System.out.println(meanTOModifiers);

        }
    }

}
        


