/**
 * Hash Table with Separate Chaining policy
 */
public class Hashtable {
    private HashNode[] bucket;
    private int bucket_size;
    private int entries;

    /**
     * Node of Chains
     */
    class HashNode{
        String key;
        String value;
        HashNode next;

        /**
         * Constructor
         * @param key the key
         * @param value the value
         */
        HashNode(String key, String value){
            this.key = key;
            this.value = value;
            next = null;
        }
    }

    /**
     * Constructor
     */
    Hashtable(){
        bucket_size = 30000;
        bucket = new HashNode[bucket_size];
        entries = 0;
    }
    /**
     * Test if a key/value object pair (with the key matching the argument and any value).
     * @param key the key
     * @return {@code true} if there is a key in the HashTable;{@code false} otherwise.
     */
    boolean containsKey(String key){
        return bucket[getHash(key)] != null;
    }

    /**
     * Returns the value associated with the key.
     * @param key the key
     * @return the value associated with the key which is passed as an argument;
     * returns ​null​ if no key/value pair is contained by the Hashtable instance.
     */
    String get(String key){
        HashNode head = bucket[getHash(key)];
        while(head != null){
            if(head.key.equals(key))
                return head.value;
            head = head.next;
        }
        return null;
    }

    /**
     * Adds the key/value pair into the Hashtable instance.
     * If there is an existing key/value pair, the Hashtable
     * instance replaces the stored value with the argument value.
     * @param key the key
     * @param value the value
     */
    void put(String key, String value) {
        HashNode head = bucket[getHash(key)];
        if (head == null)
            bucket[getHash(key)] = new HashNode(key, value);
        else {
            while (head != null) {
                if (head.key.equals(key)) {
                    head.value = value;
                    return;
                }
                head = head.next;
            }
            HashNode node = new HashNode(key,value);
            node.next = bucket[getHash(key)];
            bucket[getHash(key)] =  node;
        }
        ++entries;
        double LOAD_THRESHOLD = 0.5;
        if ((entries * 1.0) / bucket_size >= LOAD_THRESHOLD){
            increaseBucketSize();
        }
    }

    /**
     * Removes the key/value pair from the Hashtable instance.
     * @param key the key
     * @return the value associated with the key to the caller.
     * @exception NullPointerException the key is not present in the Hashtable instance.
     */
    String remove(String key) throws Exception{
        HashNode head = bucket[getHash(key)];
        if(head == null)
            throw new Exception();
        if(head.key.equals(key)) {
            bucket[getHash(key)] = head.next;
            --entries;
            return head.value;
        }
        else {
            HashNode curr;
            while(head.next != null){
                curr = head.next;
                if(curr.key.equals(key)){
                    head.next = curr.next;
                    --entries;
                    return curr.value;
                }
            }
            return head.next.value;
        }
    }

    /**
     * Hash key to an index
     * @param key the key
     * @return the String to index after hash.
     */
    private int getHash(String key){
        return Math.abs(key.hashCode())% bucket_size;
    }

    /**
     * Increase the bucket size by times 2.
     */
    private void increaseBucketSize(){
        HashNode[] temp = bucket;
        bucket_size = bucket_size *2;
        bucket = new HashNode[bucket_size];
        entries = 0;
        for(HashNode head : temp) {
            while(head != null) {
                put(head.key, head.value);
                head = head.next;
            }
        }
    }
}
