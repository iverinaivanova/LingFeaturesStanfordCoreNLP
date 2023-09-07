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
 

### Source Code + Data set
- The java-based script (**scr** folder) parses multiple research articles in the field of Computational Linguistics (CL) available in xml format from the **ACL Anthology Reference Corpus**: https://www.aclweb.org/anthology/ (Originally downloaded from the repo from: https://acl-arc.comp.nus.edu.sg/).
- The script takes xml files as input, extracts the abstract section and the full body text from each research article, and outputs the computed mean values of the linguistic features per text type.
- The data set with the features and their computed values per text type are available under the **annotations** folder.

### How to run the script
**Things to consider before you run the scripts:**
- When you open the LingFeaturesStanfordCoreNLP project in your IDE, make sure that the subbranch called **Libraries** is not empty (i.e. it should contain StanfordCoreNLP libraries that the scripts require to run the CoreNLP annotators). If the Libraries directory is empty, do the following:
1. Download Stanford CoreNLP https://stanfordnlp.github.io/CoreNLP/download.html and unarchive it.
2. Go back to the **Libraries** subbranch in your IDE, select it, and right click on it. 
3. Select **Add JAR/Folder**, search for the corenlp package that you've already unarchived and load all libraries from the folder.

