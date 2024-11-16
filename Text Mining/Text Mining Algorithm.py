import argparse
import math
import os
import random
import re
import sys
import tarfile
import time
from tqdm import tqdm
from collections import defaultdict

import nltk
import pandas as pd
import stanza
from nltk.corpus import stopwords


def parse_arguments(argv):
    """
    Parses command-line arguments.
    """
    parser = argparse.ArgumentParser(description="Text Mining Algorithm "
                                                 "Using Apriori Algorithm and StandfordNLP")

    parser.add_argument('-s', '--min-support', metavar='float', type=float, default=0.1,
                        help='Minimum support ratio (must be > 0, default: 0.1).')
    parser.add_argument('-c', '--min-confidence', metavar='float', type=float, default=0.5,
                        help='Minimum confidence (default: 0.5).')
    parser.add_argument('-k', '--sample-size', metavar='int', type=int, default=385,
                        help='Sample size (default: 385).')
    parser.add_argument('-f', '--file', metavar='path', required=True,
                        help='Path to the dataset file.')

    return parser.parse_args(argv)


def extract_texts_from_directory(directory_path):
    """
    Extracts text files from a given directory.
    """
    texts = {}
    for root, _, files in os.walk(directory_path):
        for file in files:
            if file.endswith('.txt'):
                file_path = os.path.join(root, file)
                try:
                    with open(file_path, 'r', encoding='UTF-8') as f:
                        content = f.read()
                        texts[file] = [content]
                except IOError as e:
                    print(f"Error reading file {file_path}: {e}")
    return texts


def extract_texts_from_tar_gz(tar_gz_path):
    """
    Extracts and parses .sgm files from a tar.gz archive.
    """
    texts = {}
    try:
        with tarfile.open(tar_gz_path, "r:gz") as tar:
            for member in tar.getmembers():
                if member.name.endswith('.sgm'):
                    f = tar.extractfile(member)
                    if f:
                        content = f.read().decode('ISO-8859-1')
                        texts[member.name] = extract_body_text(content)
    except (tarfile.TarError, IOError) as e:
        print(f"Error processing tar.gz file: {e}")
    return texts


def extract_body_text(sgm_content):
    """
    Extracts <BODY> content from .sgm file format.
    """
    pattern = r'<BODY>(.*?)</BODY>'
    return re.findall(pattern, sgm_content, re.DOTALL)

def sample_texts(texts_by_filename, sample_size):
    sampled_texts = []
    # Randomly sample files if the number of files exceeds sample_size
    file_keys = list(texts_by_filename.keys())
    if len(file_keys) > sample_size:
        sampled_files = random.sample(file_keys, sample_size)
    else:
        sampled_files = file_keys

    for filename in sampled_files:
        texts = texts_by_filename[filename]
        if isinstance(texts, list):  # Handling for .sgm file contents or multiple texts in a file
            if len(texts) > sample_size:
                sampled_texts.extend([(filename, text) for text in random.sample(texts, sample_size)])
            else:
                sampled_texts.extend([(filename, text) for text in texts])
        else:  # Handling for individual text files (.txt files in a folder)
            # In this case, 'texts' is a single string, so we add it directly
            sampled_texts.append((filename, texts))

    return sampled_texts

def clean_text(text, stop_words, nlp):
    # Remove HTML tags and digits
    text = re.sub(r'<.*?>|\d+|[^a-zA-Z\s]', '', text)

    # Process the text with StanfordNLP for tokenization and lemmatization
    doc = nlp(text)

    # Extract lemmatized words, filtering out stopwords and None values
    lemmatized_words = [word.lemma for sent in doc.sentences for word in sent.words if
                        word.lemma and word.lemma.strip() and word.text.lower() not in stop_words]

    # Join the lemmatized words back into a string
    lemmatized_text = ' '.join(lemmatized_words)

    return lemmatized_text

def calculate_tf_idf(texts):
    # Step 1: Calculate TF (Term Frequency) for each word in each document
    tf = defaultdict(lambda: defaultdict(int))

    for i, (_, text) in enumerate(texts):
        words = text.split()
        for word in words:
            tf[i][word] += 1

    # Step 2: Calculate DF (Document Frequency) for each word across all documents
    df = defaultdict(int)

    for i in range(len(texts)):
        unique_words = set(tf[i].keys())
        for word in unique_words:
            df[word] += 1

    # Step 3: Calculate IDF (Inverse Document Frequency) for each word
    N = len(texts)
    idf = {word: math.log(N / (df[word] + 1)) for word in df}

    # Step 4: Calculate TF-IDF scores for each word in each document
    tfidf = defaultdict(lambda: defaultdict(float))

    for i in range(len(texts)):
        for word, freq in tf[i].items():
            tfidf[i][word] = freq * idf[word]

    return tfidf

def replace_with_top_keywords(cleaned_texts, top_keywords):
    # Create a set of top keywords for easy lookup
    top_keywords_set = set(keyword for keyword, _ in top_keywords)

    # Initialize a list to hold the updated texts
    updated_texts = []

    # Iterate over each text in the cleaned_texts
    for filename, text in cleaned_texts:
        # Split the text into individual words
        words = text.split()

        # Create a set of unique keywords found in the text
        # This filters out any word not in the top_keywords_set and removes empty strings
        unique_keywords = {word for word in words if word in top_keywords_set and word.strip()}

        # Join the unique keywords with commas to create a string
        updated_text = ','.join(unique_keywords)

        # Append the tuple of filename and the updated text (string of keywords) to the updated_texts list
        updated_texts.append((filename, updated_text))

    return updated_texts

def init_apriori(dataset, min_support):
    # Declaring the lists and sets to house the 1-itemset
    list_dataset = dataset.values.tolist()
    first_candid = set()

    for row in list_dataset:
        for element in row:
            if isinstance(element, list):
                for item in element:
                    if item and not pd.isna(item):  # Check for non-null and non-empty
                        first_candid.add(item)
            else:
                if element and not pd.isna(element):  # Check for non-null and non-empty
                    first_candid.add(element)

    first_candid = sorted(list(first_candid))

    # Begin to apply the apriori principle
    count_apriori_list = count_itemsets(list_dataset, first_candid)
    total_trans = len(list_dataset)
    reduce_apriori_list = reduce_itemsets(count_apriori_list, min_support, total_trans)

    return reduce_apriori_list, count_apriori_list


def count_itemsets(list_dataset, candid_list):
    count_elem_list = {item: 0 for item in candid_list}
    for transaction in list_dataset:
        # Flatten the transaction if it's a list of lists
        if transaction and isinstance(transaction[0], list):
            flattened_transaction = [item for sublist in transaction for item in sublist]
        else:
            flattened_transaction = transaction

        for candidate in candid_list:

            if isinstance(candidate, tuple):
                # Check if the candidate tuple is a subset of the flattened transaction set
                if set(candidate).issubset(set(flattened_transaction)):
                    count_elem_list[candidate] += 1
            else:
                # Check if the single item candidate is in the flattened transaction
                if candidate in flattened_transaction:
                    count_elem_list[candidate] += 1

    return count_elem_list

def reduce_itemsets(count_itemsets, min_support, total_trans):
    # Reduce the itemsets where the support value does not meet the minimum support value
    frequent_itemsets = {}

    for items, item_count in count_itemsets.items():
        if item_count / total_trans >= min_support:
            frequent_itemsets[items] = item_count

    return frequent_itemsets

def generate_candidates(prev_frequent, k):
    candidates = set()

    # Calculate total number of iterations for the progress bar
    total_iterations = len(prev_frequent) ** 2
    progress_bar = tqdm(total=total_iterations, desc="Generating candidates")

    update_freq = 1000  # Update the progress bar every 5 iterations
    iteration_count = 0

    for prev1 in prev_frequent:
        for prev2 in prev_frequent:
            merged = sorted(list(set(prev1) | set(prev2)))
            if len(merged) == k:
                candidates.add(tuple(merged))

            iteration_count += 1
            # Update progress bar every 5 iterations or at the end
            if iteration_count % update_freq == 0 or iteration_count == total_iterations:
                progress_bar.update(min(update_freq, total_iterations - progress_bar.n))

    progress_bar.close()
    return candidates

def extract_rules(frequent_itemsets, min_confidence):
    # Begin to create the association rules for the known frequent itemsets mined
    rules = []

    for items, count in frequent_itemsets.items():
        if len(items) > 1:
            for i in range(len(items)):
                antecedent = tuple(items[:i] + items[i + 1:])
                precedent = items[i]
                conf = count / frequent_itemsets.get(antecedent, 1)

                if conf >= min_confidence:
                    # Format the rule as a string
                    rule_string = f"{set(antecedent)} => {precedent} (Confidence: {conf * 100:.2f}%)"
                    rules.append(rule_string)
    return rules

def apriori(dataset, min_support, min_confidence):
    # First level candidates
    reduced, counts = init_apriori(dataset, min_support)

    # Hold the frequent itemsets and counts
    all_frequent = {tuple([k]): v for k, v in reduced.items()}

    k = 2

    # Enter this loop if we found initial frequent itemsets
    while True:
        candidates = generate_candidates(all_frequent.keys(), k)
        if not candidates:
            break

        count = count_itemsets(dataset.values.tolist(), candidates)

        frequent = reduce_itemsets(count, min_support, len(dataset))

        if not frequent:
            break

        all_frequent.update(frequent)
        k += 1

    # Extract association rules
    rules = extract_rules(all_frequent, min_confidence)

    return all_frequent, rules

def prepare_data_for_apriori(cleaned_texts):
    # Convert the list of tuples into a DataFrame
    df = pd.DataFrame.from_records(cleaned_texts, columns=['Filename', 'Keywords'])
    df['Keywords'] = df['Keywords'].str.split(',')

    # Drop the Filename as it's not needed for Apriori
    df = df.drop(columns=['Filename'])

    return df

def main():
    args = parse_arguments(sys.argv[1:])

    # Download NLTK stopwords
    nltk.download('stopwords')

    # Set of English stopwords
    stop_words = set(stopwords.words('english'))

    # Initializing Stanford CoreNLP
    stanza.download('en')
    nlp = stanza.Pipeline(lang='en', processors='tokenize,pos,lemma')

    # Extract texts by filename
    if args.file.endswith('.tar.gz'):
        print("Extracting all texts from each .sgm file in .tar.gz file.")
        texts_by_filename = extract_texts_from_tar_gz(args.file)
    else:
        print("Extracting all .txt files from folder path.")
        texts_by_filename = extract_texts_from_directory(args.file)

    # Flatten the texts_by_filename to a list of tuples (filename, text)
    all_texts = [(filename, text) for filename, texts in texts_by_filename.items() for text in texts]

    # Decide whether to use all texts or perform random sampling
    if args.sample_size >= len(all_texts):
        print("\nUsing all available texts without sampling.")
        sampled_texts = all_texts
    else:
        print(f"\nNow commencing random sampling process, k = {args.sample_size}.")
        sampled_texts = random.sample(all_texts, args.sample_size)


    # Cleaning and Lemmatizing words
    print("\nNow cleaning and lemmatizing words to extract relevant keywords from document database.")

    cleaned_texts = []
    interval_time = time.time() #record the start time

    for count, (filename, text) in enumerate(sampled_texts):
        cleaned_text = clean_text(text, stop_words, nlp)
        cleaned_texts.append((filename, cleaned_text))

        # Check if 120 seconds have passed and print the message
        if time.time() - interval_time >= 120:
            print("\nProcessing dataset...")
            interval_time = time.time()  # Update the last_time to the current time

    print("\nCalculating tf-idf scores for each keyword.")
    tfidf_scores = calculate_tf_idf(cleaned_texts)
    top_keywords = []

    print("\nRanking top keywords based on tf-idf scores and taking the top 200 keywords for the document database.")
    for i, _ in enumerate(cleaned_texts):
        top_keywords.extend([(word, score) for word, score in tfidf_scores[i].items()])

    top_keywords = sorted(top_keywords, key=lambda x: x[1], reverse=True)[:200]

    cleaned_texts_with_keywords = replace_with_top_keywords(cleaned_texts, top_keywords)

    print(f"\nFirst 10 documents being represented by td-idf ranked keywords:", cleaned_texts_with_keywords[:10])

    # Prepare data for Apriori
    prepared_data = prepare_data_for_apriori(cleaned_texts_with_keywords)

    # Run Apriori algorithm
    print("\nBeginning Apriori Algorithm to generate frequent itemsets and association rules.")
    freq_itemsets, rules = apriori(prepared_data, args.min_support, args.min_confidence)

    # Create a single string to list all frequent itemsets
    itemsets_str = ', '.join([f"{set(itemset)}" for itemset, count in freq_itemsets.items()])

    print("Frequent Itemsets:")
    print(itemsets_str)

    if rules:
        print(f"Rules:")
        for rule in rules:
            print(rule)
    else:
        print("No rules found for this confidence/support value.")

if __name__ == '__main__':
    main()
