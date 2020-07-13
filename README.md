## LingFeaturesStanfordCoreNLP

### Overview of the project
LingFeaturesStanfordCoreNLP contains a java scripts (**src** folder) that parses multiple articles in Compulational Linguistics in xml form (the files can be accessed from the **all_Ks** folder. Source: ACL Anthology Reference Corpus), and uses the Stanford CoreNLP Module to output coreference analysis per document (mean number of coreference chains), as well as text length, sentence count, number of self mentions, 

- Note that the all_Ks does not contain the whole corpus.

- The data set with the features and their computed values per text type are available under the **annotations** folder.

### How to run the scripts and what the scripts do
**Things to consider before you run the scripts:**
- When you open the MycoreNLPDemo project in your editor, make sure that the subbranch called **Libraries** in not empty (i.e. it contains Stanford CoreNLP libraries that the scripts require to run the CoreNLP annotators).If the Libraries directory is empty, do the following:
- Download Stanford CoreNLP https://stanfordnlp.github.io/CoreNLP/download.html and unarchive it.
- Then go back to the **Libraries** subbranch, select it, and right click on it. Then select **Add JAR/Folder**, search for the corenlp package that you've already unarchived and load all libraries from the folder.

