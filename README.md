# Simple Java Based Bloom Filter
> Gradle based Java Application for a simple bloom filter
## Usage

```java
BloomFilter bloomFilter = BloomFilter.getInstance();
bloomFilter.add("messi");
bloomFilter.add("ronaldo");
System.out.println(bloomFilter.check("neymar")); // false
System.out.println(bloomFilter.check("ronaldo")); // true
```
