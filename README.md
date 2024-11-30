# adobe-interview-2024
2024 Adobe Interview

## Compilation + running

### To Build:
``mvn clean package``

### To Run:
NOTE: Use `-i` option to specify the input file. `-o` is optional and will output the file.
Logs are in the `logs/app.log` directory.

``java -jar target/xuelian-adobe-interview-1.0-SNAPSHOT.jar -i src/test/resources/code_challenge_leads.json -o deduplicated.out``

## General Algorithm
We consider two records to be duplicates if either:
1) Email match
2) IDs match

Therefore, we do the following:
1) Create two maps. 
   a) Map of Email -> List<Record_Indexes>
   b) Map of ID -> List<Record_Indexes>
2) Use union find, and mark all records with the same email or ID as part of the same set (used for deduplication)
3) When determining parent of the set, use the one that has the latest EntryDate. 
If two records have the same EntryDate, use list index as the tiebreaker.
4) Only add the parent values of the union find into the deduplicated set
