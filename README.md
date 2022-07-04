## LingFeaturesStanfordCoreNLP

### Overview of the project
The aim of the project is to identify distinctive linguistic features for two text types -- abstracts and full texts -- appearing in the same corpus of a reseach article. To achieve this, we automatically extracted a predefined set of linguistic features from multiple research articles using the StanfordCoreNLP Module. 

## Linguistic Features
- coreference (measured by frequency of coreference chains)
- noun density
- noun phrase (NP) count
- self-mentions
- NP complexity (measured by frequency of embedded clauses in the internal structure of an NP)
 - embedded that-clause
 - embedded to-infinitival clause
 - embedded past-participial clause
- sentence length (used for normalization purposes)
 


### Source Code
The source code is available in the **scr** folder. It parses multiple articles in the field of Computational Linguistics (CL) available in xml format from the [ACL Anthology Reference Corpus](https://acl-arc.comp.nus.edu.sg/)). The **all_Ks** folder contains example articles to illustrate the structure of the parsed articles.

and uses [the Stanford CoreNLP Module](https://stanfordnlp.github.io/CoreNLP/index.html) to output:
- coreference analysis per document (measured in mean number of coreference chains),
- mean number of tokens
- mean sentence length
- mean number of self mentions, mean number of NPs, mean number of finite and non-finite clauses embedded in the NP structure.

- Note that the **all_Ks** does not contain the whole corpus.

- The data set with the features and their computed values per text type are available under the **annotations** folder.

### How to run the script
**Things to consider before you run the scripts:**
- When you open the LingFeaturesStanfordCoreNLP project in your editor, make sure that the subbranch called **Libraries** in not empty (i.e. it contains Stanford CoreNLP libraries that the scripts require to run the CoreNLP annotators).If the Libraries directory is empty, do the following:
- Download Stanford CoreNLP https://stanfordnlp.github.io/CoreNLP/download.html and unarchive it.
- Then go back to the **Libraries** subbranch, select it, and right click on it. Then select **Add JAR/Folder**, search for the corenlp package that you've already unarchived and load all libraries from the folder.

