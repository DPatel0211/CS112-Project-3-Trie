package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {

    public static ArrayList<TrieNode> hm1(TrieNode r, ArrayList<TrieNode> pList) {
        while (r != null) {
            if (r.firstChild != null) {
                pList = hm1(r.firstChild, pList);
            } else {
                pList.add(r);
            }
            r = r.sibling;
        }
        return pList;
    }
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
        if (allWords.length == 0) {
            return null;
        }

		TrieNode r = new TrieNode(null, null, null);

        for (int i = 0; i < allWords.length; i++) {
            if (i == 0) {
                short x = 0;
                short y = 0;
                for (short j = 0; j < allWords[i].length(); j++) {
                    if (j + 1 == allWords[i].length()) {
                        y = j;
                    }
                }
                Indexes ind = new Indexes(0, x, y);
                TrieNode wNode = new TrieNode(ind, null, null);
                r.firstChild = wNode;
                continue;
            }

            TrieNode ptr = r.firstChild;
            TrieNode prev = r;
            String cWord = allWords[i];
            short sPoint = 0;

            while (ptr != null) {
                if (cWord.charAt(sPoint) == allWords[ptr.substr.wordIndex].charAt(sPoint)) {

                    short end = 0;
                    for (short j = sPoint; j < cWord.length(); j++) {
                        if (cWord.charAt(j) != allWords[ptr.substr.wordIndex].charAt(j)) {
                            break;
                        }
                        if (j > ptr.substr.endIndex) {
                            break;
                        }
                        end = j;
                    }
                    if (end == ptr.substr.endIndex) {
                        sPoint = (short) ((short) end + 1);
                        prev = ptr;
                        ptr = ptr.firstChild;
                        continue;
                    } else {
                        Indexes ind = new Indexes(ptr.substr.wordIndex, sPoint, end);
                        TrieNode n = new TrieNode(ind, null, null);
                        Indexes cIndex = new Indexes(ptr.substr.wordIndex, (short) ((short) end + 1), ptr.substr.endIndex);
                        TrieNode cNode = new TrieNode(cIndex, null, null);
                        Indexes sIndex = new Indexes(i, (short) ((short) end + 1), (short) ((short)cWord.length() - 1));
                        TrieNode sNode = new TrieNode(sIndex, null, null);

                        if (prev == r || prev.firstChild == ptr) {
                            prev.firstChild = n;
                        } else {
                            prev.sibling = n;
                        }

                        cNode.sibling = sNode;
                        n.firstChild = cNode;
                        n.sibling = ptr.sibling;

                        if (ptr.firstChild != null) {
                            cNode.firstChild = ptr.firstChild;
                        }
                        break;
                    }
                } else if (ptr.sibling == null && cWord.charAt(sPoint) != allWords[ptr.substr.wordIndex].charAt(sPoint)){
                    short end = 0;

                    for (short j = sPoint; j < cWord.length(); j++) {
                        end = j;
                    }

                    Indexes index = new Indexes(i, sPoint, end);
                    TrieNode n = new TrieNode(index, null, null);
                    ptr.sibling = n;
                    break;

                } else {
                    prev = ptr;
                    ptr = ptr.sibling;
                }
            }
        }

        return r;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
        TrieNode ptr = root.firstChild;
        if (allWords.length == 0 || ptr == null || prefix.length() == 0) {
            return null;
        }
        String s = "" ;
        ArrayList<TrieNode> pList = new ArrayList<>();

        while (ptr != null) {
            if (prefix.charAt(ptr.substr.startIndex) != allWords[ptr.substr.wordIndex].charAt(ptr.substr.startIndex)) {
                ptr = ptr.sibling;
            } else {
                for (int i = ptr.substr.startIndex; i <= ptr.substr.endIndex; i++) {
                    if (i == prefix.length()) {
                        break;
                    }
                    if (prefix.charAt(i) != allWords[ptr.substr.wordIndex].charAt(i)) {
                        return null;
                    }

                    s += prefix.charAt(i);

                    if (s.equals(prefix)) {
                        break;
                    }
                }

                if (s.equals(prefix)) {
                    if (ptr.firstChild == null) {
                        pList.add(ptr);
                        break;
                    }

                    TrieNode r = ptr.firstChild;
                    pList = hm1(r, pList);
                    break;
                } else {
                    ptr = ptr.firstChild;
                    continue;
                }
            }
        }

        if (s == null || s == "" || !(s.equals(prefix))) {
            return null;
        }
        return pList;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
