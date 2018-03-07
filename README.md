**JEXM** - Java Excel Mapper
===================

JEXM is an Open-Source library to facilitate reading from Excel files in an efficient and intuitive way.

The JEXM API is simple to use, making use the latest [Java 8 Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)
library and [Apache POI](https://poi.apache.org/) to allow for low memory footprint, parallel execution and powerful functional programming
 constructs.

<br />

Usage
-------------

Assuming an Excel file named *people.xlsx* in the resources folder exists with the following contents:


Id | Name     | Middlename | Lastname | Date of Birth | Gender | Score | Audition time | Role    | Favourite Colors 
---|----------|------------|----------|---------------|--------|-------|---------------|---------| ---------------- 
1  | Chuck    | Albert     | Smith    | 10/03/1940    | MALE   | 100   | 18:00		   | Fighter |
2  | Bruce    |            | Johnson  | 29/11/1940    | MALE   | 75.5  | 16:45	       | Fighter |
3  | Michael  | Davis      | Jones    | 29/08/1958    | MALE   | 60.75 | 12:15         | Dancer  | Red, Black
4  | Taylor   |            | Williams | 13/12/1989    | FEMALE | 70.85 | 11:20         | Singer  | Purple, White



The user may create the following Java bean to define the mapping of cell values given each header:
```java
class Person {

    @Header(name = "Id")               private int id;
    @Header(name = "Name")             private String name;
    @Header(name = "Lastname")         private String lastname;
    @Header(name = "Date of Birth")    private LocalDate dob;
    @Header(name = "Score")            private double score;
    @Header(name = "Audition time")    private LocalTime auditionTime;
    @Header(name = "Role")             private Role role;
    @Header(name = "Favourite Colors") private Color[] favouriteColors;

    private enum Role { SINGER, DANCER, FIGHTER }
    private enum Color { BLACK, WHITE, RED, BLUE, PURPLE, GREEN, ORANGE, YELLOW }

    @Override
    public String toString() {
        return "(" + id + ") " + name + " " + lastname + ", born in "
                + dob + ", score " + score+ ", has audition at " + auditionTime
                + " for role " + role + ", favourite colors: " + Arrays.toString(favouriteColors);
    }

}
```


By calling the stream method of the JEXM API, passing in the bean class and filepath:

```java
try(Stream<Person> s = JEXMContext.newInstance(Person.class).read("resources/people.xlsx")){
   s.forEach(System.out::println);
}
```

Will produce the following result:

```
(1) Chuck Smith, born in 1940-03-10, score 100.0, has audition at 18:00 for role FIGHTER, favourite colors: []
(2) Bruce Johnson, born in 1940-11-29, score 75.5, has audition at 16:45 for role FIGHTER, favourite colors: []
(3) Michael Jones, born in 1958-08-29, score 60.75, has audition at 12:15 for role DANCER, favourite colors: [RED, BLACK]
(4) Taylor Williams, born in 1989-12-13, score 70.85, has audition at 11:20 for role SINGER, favourite colors: [PURPLE, WHITE]
```

<br />


The JEXMContext class has the following public methods, where <i>T</i> is a serializable type expected to contain
the *@Header* annotation in it's instance variables <i class="icon-code"></i>:
```java
Stream<T> read(String filepath)
Stream<T> read(String filepath, ExcelExtension excelExtension)
Stream<T> read(Path filepath)
Stream<T> read(Path filepath, ExcelExtension excelExtension)
Stream<T> read(File file)
Stream<T> read(File file, ExcelExtension excelExtension)
Stream<T> read(InputStream inputStream, ExcelExtension excelExtension)
Stream<T> read(RawRowIterator rawRowIterator)
```

Where the first parameter represents the bean class for mapping of each row in the document, containing **@Header**
annotations in its fields (see above). The second parameter is a path to the Excel file to be parsed
(or its *InputStream*) and the optional third is used to outline the type of file from the supported extensions.
If the third parameter is not present or is **null**, the file type is figured out from its extension.
The last option allows users to define their own low-level file parsers through `RawRowIterator`, allowing them to add
support for custom or unsupported file types.

Similarly to [java.nio.Files.lines(...)](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html#lines-java.nio.file.Path-), the stream needs to be closed by the caller to avoid leaking of IO resources, which can be done using the **try-with-resources** construct as above. The possibility of a self-closing stream was taken into account, but is error-prone and does not comply with the design principle where the acquirer of a resource should be the one to release it.

<br />

Supported file extensions
-------------
JEXM currently supports parsing of the following file types:

-  **CSV** - Comma Separated Values
- **XLSX** - Open XML Spreadsheet for Microsoft Office 2007
- **XLSM** - Open XML Spreadsheet for Microsoft Office 2007 with Macros

Future support: **XLS** - Excel Spreadsheet for Microsoft Office 2003 & **ODS** - OpenDocument Spreadsheet

<br />

Annotations
-------------

### @Header

The *@Header* annotation is used to tell JEXM which header/column should be used to update a field in the Java bean. It contains the following parameters. Only up to one of them can be set for an annotated field, else an *IllegalHeaderException* is thrown. If none are specified, the annotated field name itself is used for the header lookup.

- **name**: String used to perform the mapping based on the contents of the first row, which are used as lookup names for each column. If this value is not defined (and the index is also not defined), the annotated variable name itself is used.
- **index**: Integer used to perform the mapping based on the index of the columns starting from zero. Both name and index cannot be set in the same annotation.
- **ref**: String used to perform the mapping based on the column reference in Excel ("A", "B", ... "AZ", "BA", ...). This is also compatible with CSV files.

<br />

### @Adapter

The *@Adapter* annotation is an optional addition to a field already annotated by *@Header*. It is used to manipulate how the raw value of an Excel cell is mapped to a field in the Java bean.

- **value**: A class which extends *CellAdapter* used if the user wants to specify their own adapter rules when parsing the raw String cell. This is useful if the user wants to adapt a field which isn't supported by the built-in adapters (see section below) or if they want to apply other transformations to the field.
- **suppressAdapterException**: Boolean used to define whether exceptions when adapting a raw cell in the document should result on an exception or simply be suppressed (default). Example: adapting "Hello World" to an Integer will throw a *NumberFormatException* if this field is set to *false*, else it will be logged by JEXM and move on.

<br />

### @Sheet

The *@Sheet* annotation is an optional addition to the Java bean class. It provides the ability to select which sheet in the Excel workbook will be parsed when generating the stream. This annotation is ignored when processing CSV files. For XLSX files, if this annotation is not present, the first sheet is parsed by default.

- **name**: The excel sheet name to be chosen for parsing.
- **index**: The excel sheet index to be chosen for parsing (starting from 0).
<br />


Supported field types
-------------
The following field types have built-in adapters in JEXM. This means they will automatically be converted from the
raw file data to the given Java field type. If an unsupported field is annotated by *@Header*, an *UnsupportedAdapterFieldException* will be thrown unless the user explicitly defines their own adapting rules by making use of the *@Adapter* annotation.   Please explore package *com.americanexpress.jexm.adapter* for more details.

**String**

- `java.lang.String`
- `java.lang.StringBuilder`
- `java.lang.StringBuffer`

**Primitives**

- `boolean`
- `byte`
- `char`
- `double`
- `float`
- `int`
- `long`
- `short`

**Boxed primitives**

- `java.lang.Boolean`
- `java.lang.Byte`
- `java.lang.Character`
- `java.lang.Double`
- `java.lang.Float`
- `java.lang.Integer`
- `java.lang.Long`
- `java.lang.Short`

**Date and time**

- `java.time.LocalDate`
- `java.time.LocalTime`
- `java.time.LocalDateTime`
- `java.time.Instant`
- `java.time.MonthDay`
- `java.time.YearMonth`
- `java.util.Date`

**SQL**

- `java.sql.Date`
- `java.sql.Time`
- `java.sql.Timestamp`

**Other**

- `java.math.BigInteger`
- `java.math.BigDecimal`
- `java.util.regex.Pattern`
- `java.lang.Class`

**Enums**

Any existing or user-defined enums are supported.

**Arrays and collections**

Single dimensional arrays of any of the above types are supported.
The following collections of any of the above types are supported:

- `java.util.Collection`
- `java.util.AbstractCollection`
- `java.util.List`
- `java.util.AbstractList`
- `java.util.ArrayList`
- `java.util.LinkedList`
- `java.util.Queue`
- `java.util.ArrayDequeue`
- `java.util.Vector`
- `java.util.Stack`
- `java.util.Set`
- `java.util.HashSet`
- `java.util.LinkedHashSet`
- `java.util.TreeSet`
- `java.util.AbstractDequeue`
- `java.util.concurrent.BlockingDeque`
- `java.util.concurrent.LinkedBlockingDeque`
- `java.util.concurrent.CopyOnWriteArrayList`
- `java.util.concurrent.CopyOnWriteArraySet`
- `java.util.concurrent.SynchronousQueue`
- `java.util.concurrent.ConcurrentSkipListSet`

----------

## Contributing
We welcome Your interest in the American Express Open Source Community on Github.
Any Contributor to any Open Source Project managed by the American Express Open
Source Community must accept and sign an Agreement indicating agreement to the
terms below. Except for the rights granted in this Agreement to American Express
and to recipients of software distributed by American Express, You reserve all
right, title, and interest, if any, in and to Your Contributions. Please [fill
out the Agreement](https://cla-assistant.io/americanexpress/jexm).

Please feel free to open pull requests and see [CONTRIBUTING.md](./CONTRIBUTING.md) for commit formatting details.

## License
Any contributions made under this project will be governed by the [Apache License 2.0](./LICENSE.txt).

## Code of Conduct
This project adheres to the [American Express Community Guidelines](./CODE_OF_CONDUCT.md). By participating, you are
expected to honor these guidelines.
