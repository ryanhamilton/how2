How2
================================

Allows searching stackoverflow.com from the command line and viewing the code solutions.
Based on same idea as https://github.com/santinic/how2 I couldn't get the npm to install so I wrote my own version in java.

It's a single file that I place in and then alias.



Usage
-------------------------

how2 "search query" [numberOfResultToDisplay] [open]

One argument  e.g. how2 "java hello world" = Searches google for stackoverflow results.
Two arguments e.g. how2 "java hello world" 0 = Show the first result
Three arguments e.g. how2 "java hello world" 0 open = Open that page in browser


Install Linux
------------------------

```
mkdir -p ~/bin
wget -q -O bin/how2.jar jpad.io/how2.jar 
echo "alias how2='java -jar ~/bin/how2.jar'" >> ~/.bashrc
source ~/.bashrc
```





Examples
-------------------------

```
> java -jar how2.jar "bash set environment variable" 0
Setting environment variables in Linux using bash - Stack Overflow
----------------------------------
export VAR=value will set VAR to value. Enclose it in single quotes if you want spaces, like export VAR='my val'. If you want the variable to be interpolated, use double quotes, like export VAR="$MY_OTHER_VAR".
export VAR=value
export VAR='my val'
export VAR="$MY_OTHER_VAR"

```




```
> java -jar how2.jar "java write file"
0. How do I create a file and write to a file in Java?
1. java writing to a text file
2. How to Write text file Java
3. How do I save a String to a text file using Java?
4. What is the simplest way to write a text file in java
5. java - How to write console output to a txt file
6. json - Write String to text-file in java

How do I create a file and write to a file in Java? - Stack Overflow
----------------------------------
try{
    PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
    writer.println("The first line");
    writer.println("The second line");
    writer.close();
} catch (IOException e) {
   // do something
}
byte data[] = ...
FileOutputStream out = new FileOutputStream("the-file-name");
out.write(data);
out.close();
Files
List<String> lines = Arrays.asList("The first line", "The second line");
Path file = Paths.get("the-file-name.txt");
Files.write(file, lines, Charset.forName("UTF-8"));
//Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
byte data[] = ...
Path file = Paths.get("the-file-name");
Files.write(file, data);

...
```








