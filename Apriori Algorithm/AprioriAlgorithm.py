import pandas as pd
import argparse
import sys
import time

def init_apriori(dataset, min_support):
    # Declaring the lists and sets to house the 1-itemset
    list_dataset = dataset.values.tolist()
    first_candid = set()

    for row in list_dataset:
        for element in row:
            if not pd.isna(element):
                first_candid.add(element)

    first_candid = sorted(first_candid)

    # Begin to apply the apriori principle
    count_apriori_list = count_itemsets(list_dataset, first_candid)
    total_trans = len(list_dataset)
    reduce_apriori_list = reduce_itemsets(count_apriori_list, min_support, total_trans)

    return reduce_apriori_list, count_apriori_list

def count_itemsets(list_dataset, candid_list):
    candid_list = list(candid_list)
    count_elem_list = {item: 0 for item in candid_list}

    if isinstance(candid_list[0], tuple):
        # Begin to count the occurrences of the k+1 itemset
        for transaction in list_dataset:
            for candidate in candid_list:
                if set(candidate).issubset(set(transaction)):
                    count_elem_list[candidate] += 1
    else:
        # Enter this if it is the first time in counting the itemset
        for transaction in list_dataset:
            for candidate in candid_list:
                if pd.isna(candidate):
                    continue
                count_elem_list[candidate] += transaction.count(candidate)

    return count_elem_list

def reduce_itemsets(count_itemsets, min_support, total_trans):
    # Reduce the itemsets where the support value does not meet the minimum support value
    frequent_itemsets = {}

    for items, item_count in count_itemsets.items():
        if item_count / total_trans >= min_support:
            frequent_itemsets[items] = item_count

    return frequent_itemsets

def generate_candidates(prev_frequent, k):
    # Generates possible new frequent itemsets
    candidates = set()

    for prev1 in prev_frequent:
        for prev2 in prev_frequent:
            merged = sorted(list(set(prev1) | set(prev2)))

            if len(merged) == k:
                candidates.add(tuple(merged))

    return candidates

def extract_rules(frequent_itemsets, min_confidence):
    # Begin to create the association rules for the known frequent itemsets mined
    rules = []

    for items, count in frequent_itemsets.items():
        if len(items) > 1:
            for i in range(len(items)):
                antecedent = list(items[:i]) + list(items[i + 1:])
                precedent = items[i]
                conf = count / frequent_itemsets.get(tuple(antecedent), 1)

                if conf >= min_confidence:
                    rules.append((antecedent, precedent, conf))

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


def parse_args(argv):
    parser = argparse.ArgumentParser()

    parser.add_argument(
        '-s', '--min-support', metavar='float',
        help='Minimum support ratio (must be > 0, default: 0.1).',
        type=float, default=0.1)
    parser.add_argument(
        '-c', '--min-confidence', metavar='float',
        help='Minimum confidence (default: 0.5).',
        type=float, default=0.5)
    parser.add_argument(
        '-f', '--file', metavar='path',
        help='Path to the CSV dataset.',
        required=True)

    args = parser.parse_args(argv)
    return args


def main():
    args = parse_args(sys.argv[1:])
    dataset = pd.read_csv(args.file, header=None)

    # Sterilizing data in-place
    dataset = dataset.fillna('')
    dataset = dataset.apply(lambda row: [item for item in row if item != ''], axis=1)

    print("Processing Dataset:")
    start_apr_time = time.time()
    freq_itemsets, rules = apriori(dataset, args.min_support, args.min_confidence)
    end_apr_time = time.time()

    print("Transactions:", dataset.values.tolist())

    if not rules:
        print("\nNo association rules could be generated for this dataset.")
    else:
        print("\nAssociation Rules:")
        for r in rules:
            print(f"{r[0]} => {r[1]}, confidence = {r[2]:.2f}")

    elapsed_apr_time = end_apr_time - start_apr_time  # Capture compute time
    print(f"Elapsed Apriori Algorithm Time: {elapsed_apr_time:.6f} seconds\n")


if __name__ == '__main__':
    main()