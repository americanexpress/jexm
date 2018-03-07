/*
 * Copyright (c) 2018 American Express Travel Related Services Company, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.americanexpress.jexm.integration;

import static org.junit.Assert.assertEquals;

import com.americanexpress.jexm.ExcelExtension;
import com.americanexpress.jexm.FileToTest;
import com.americanexpress.jexm.JEXMContext;
import com.americanexpress.jexm.adapter.exceptions.CellAdapterException;
import com.americanexpress.jexm.adapter.exceptions.UnsupportedAdapterFieldException;
import com.americanexpress.jexm.mapping.exceptions.IllegalHeaderException;
import com.americanexpress.jexm.resources.beans.array.ArrayOfBigIntegers;
import com.americanexpress.jexm.resources.beans.array.ArrayOfIntegers;
import com.americanexpress.jexm.resources.beans.array.ArrayOfInts;
import com.americanexpress.jexm.resources.beans.array.ArrayOfStrings;
import com.americanexpress.jexm.resources.beans.collection.ArrayListOfIntegers;
import com.americanexpress.jexm.resources.beans.collection.ArrayListOfStrings;
import com.americanexpress.jexm.resources.beans.collection.ListOfIntegers;
import com.americanexpress.jexm.resources.beans.collection.ListOfStrings;
import com.americanexpress.jexm.resources.beans.people.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.Test;

public abstract class TestJexmAbstract {

  public abstract ExcelExtension extension();

  protected <T extends Serializable> void assertStreamProducesResult(
      Class<T> pojoClass, FileToTest testFile, List<T> expected) {
    List<T> streamResult = streamToList(pojoClass, testFile);
    assertEquals(expected, streamResult);
  }

  protected <T extends Serializable> List<T> streamToList(Class<T> pojoClass, FileToTest testFile) {
    try (Stream<T> s = JEXMContext.newInstance(pojoClass).read(testFile.path(extension()))) {
      return s.collect(Collectors.toList());
    }
  }

  @Test
  public final void testStreamMethodWithClassAndStringParametersShouldProcessCorrectly() {
    String filepath = FileToTest.PERSON_FIRST_NAMES.path(extension()).toString();

    try (Stream<PersonFirstName> s =
        JEXMContext.newInstance(PersonFirstName.class).read(filepath)) {
      assertEquals(3, s.count());
    }
  }

  @Test
  public final void testStreamMethodWithClassStringAndExtensionParametersShouldProcessCorrectly() {
    String filepath = FileToTest.PERSON_FIRST_NAMES.path(extension()).toString();

    try (Stream<PersonFirstName> s =
        JEXMContext.newInstance(PersonFirstName.class).read(filepath, extension())) {
      assertEquals(3, s.count());
    }
  }

  @Test
  public final void testStreamMethodWithClassAndPathParametersShouldProcessCorrectly() {
    Path path = FileToTest.PERSON_FIRST_NAMES.path(extension());

    try (Stream<PersonFirstName> s = JEXMContext.newInstance(PersonFirstName.class).read(path)) {
      assertEquals(3, s.count());
    }
  }

  @Test
  public final void testStreamMethodWithClassPathAndExtensionParametersShouldProcessCorrectly() {
    Path path = FileToTest.PERSON_FIRST_NAMES.path(extension());

    try (Stream<PersonFirstName> s =
        JEXMContext.newInstance(PersonFirstName.class).read(path, extension())) {
      assertEquals(3, s.count());
    }
  }

  @Test
  public final void testStreamMethodWithClassAndFileParametersShouldProcessCorrectly() {
    File file = FileToTest.PERSON_FIRST_NAMES.path(extension()).toFile();

    try (Stream<PersonFirstName> s = JEXMContext.newInstance(PersonFirstName.class).read(file)) {
      assertEquals(3, s.count());
    }
  }

  @Test
  public final void testStreamMethodWithClassFileAndExtensionParametersShouldProcessCorrectly() {
    File file = FileToTest.PERSON_FIRST_NAMES.path(extension()).toFile();

    try (Stream<PersonFirstName> s =
        JEXMContext.newInstance(PersonFirstName.class).read(file, extension())) {
      assertEquals(3, s.count());
    }
  }

  @Test
  public final void
      testStreamMethodWithClassInputStreamAndExtensionParametersShouldProcessCorrectly()
          throws IOException {

    InputStream inputStream = Files.newInputStream(FileToTest.PERSON_FIRST_NAMES.path(extension()));

    try (Stream<PersonFirstName> s =
        JEXMContext.newInstance(PersonFirstName.class).read(inputStream, extension())) {
      assertEquals(3, s.count());
    }
  }

  @Test(expected = NullPointerException.class)
  public final void
      testStreamMethodWithClassInputStreamAndNullExtensionParametersShouldThrowException()
          throws IOException {

    InputStream inputStream = Files.newInputStream(FileToTest.PERSON_FIRST_NAMES.path(extension()));

    try (Stream<PersonFirstName> s =
        JEXMContext.newInstance(PersonFirstName.class).read(inputStream, null)) {
      assertEquals(3, s.count());
    }
  }

  @Test
  public final void testPeopleWithLotsOfFieldTypes() {
    List<PersonLotsOfInfo> expected =
        Arrays.asList(
            new PersonLotsOfInfo.Builder()
                .id(1)
                .name("Chuck")
                .middlename("Albert")
                .lastname("Smith")
                .dob(LocalDate.of(1940, 3, 10))
                .gender(PersonLotsOfInfo.Gender.MALE)
                .score(100)
                .auditionTime(LocalTime.of(18, 0))
                .role(PersonLotsOfInfo.Role.FIGHTER)
                .favouriteColours(new PersonLotsOfInfo.Color[0])
                .build(),
            new PersonLotsOfInfo.Builder()
                .id(2)
                .name("Bruce")
                .middlename(null)
                .lastname("Johnson")
                .dob(LocalDate.of(1940, 11, 29))
                .gender(PersonLotsOfInfo.Gender.MALE)
                .score(75.5)
                .auditionTime(LocalTime.of(16, 45))
                .role(PersonLotsOfInfo.Role.FIGHTER)
                .favouriteColours(new PersonLotsOfInfo.Color[0])
                .build(),
            new PersonLotsOfInfo.Builder()
                .id(3)
                .name("Michael")
                .middlename("Davis")
                .lastname("Jones")
                .dob(LocalDate.of(1958, 8, 29))
                .gender(PersonLotsOfInfo.Gender.MALE)
                .score(60.75)
                .auditionTime(LocalTime.of(12, 15))
                .role(PersonLotsOfInfo.Role.DANCER)
                .favouriteColours(
                    new PersonLotsOfInfo.Color[] {
                      PersonLotsOfInfo.Color.RED, PersonLotsOfInfo.Color.BLACK
                    })
                .build(),
            new PersonLotsOfInfo.Builder()
                .id(4)
                .name("Taylor")
                .middlename(null)
                .lastname("Williams")
                .dob(LocalDate.of(1989, 12, 13))
                .gender(PersonLotsOfInfo.Gender.FEMALE)
                .score(70.85)
                .auditionTime(LocalTime.of(11, 20))
                .role(PersonLotsOfInfo.Role.SINGER)
                .favouriteColours(
                    new PersonLotsOfInfo.Color[] {
                      PersonLotsOfInfo.Color.PURPLE, PersonLotsOfInfo.Color.WHITE
                    })
                .build());

    assertStreamProducesResult(PersonLotsOfInfo.class, FileToTest.PERSON_LOTS_OF_INFO, expected);
  }

  @Test
  public final void testPeopleLookingUpByHeaderNameWithSingleHeaderShouldPopulateValues() {
    List<PersonFirstName> expected =
        Arrays.asList(
            new PersonFirstName("Chuck"),
            new PersonFirstName("Bruce"),
            new PersonFirstName("Michael"));

    assertStreamProducesResult(PersonFirstName.class, FileToTest.PERSON_FIRST_NAMES, expected);
  }

  @Test
  public final void testPeopleWithDuplicateHeadersShouldOnlyUseFirstOccurrence() {
    List<PersonFirstName> expected =
        Arrays.asList(
            new PersonFirstName("Chuck"),
            new PersonFirstName("Bruce"),
            new PersonFirstName("Michael"));

    assertStreamProducesResult(
        PersonFirstName.class, FileToTest.PERSON_FIRST_NAMES_ILLEGAL_DUPLICATE_HEADERS, expected);
  }

  @Test
  public final void testPeopleLookingUpByHeaderNameWithMultipleHeadersShouldPopulateValues() {
    List<PersonFullnameHeaderNames> expected =
        Arrays.asList(
            new PersonFullnameHeaderNames("Chuck", "Albert", "Smith"),
            new PersonFullnameHeaderNames("Bruce", null, "Johnson"),
            new PersonFullnameHeaderNames("Michael", "Davis", "Jones"));

    assertStreamProducesResult(
        PersonFullnameHeaderNames.class, FileToTest.PERSON_NAMES_FILE, expected);
  }

  @Test
  public final void testPeopleLookingUpByHeaderIndexesShouldPopulateValues() {
    List<PersonFullnameHeaderIndexes> expected =
        Arrays.asList(
            new PersonFullnameHeaderIndexes("Chuck", "Albert", "Smith"),
            new PersonFullnameHeaderIndexes("Bruce", null, "Johnson"),
            new PersonFullnameHeaderIndexes("Michael", "Davis", "Jones"));

    assertStreamProducesResult(
        PersonFullnameHeaderIndexes.class, FileToTest.PERSON_NAMES_FILE, expected);
  }

  @Test
  public final void testPeopleLookingUpByHeaderNamesAndIndexesShouldPopulateValuesAccordingly() {
    List<PersonFullnameHeaderNamesAndIndexes> expected =
        Arrays.asList(
            new PersonFullnameHeaderNamesAndIndexes("Chuck", "Albert", "Smith"),
            new PersonFullnameHeaderNamesAndIndexes("Bruce", null, "Johnson"),
            new PersonFullnameHeaderNamesAndIndexes("Michael", "Davis", "Jones"));

    assertStreamProducesResult(
        PersonFullnameHeaderNamesAndIndexes.class, FileToTest.PERSON_NAMES_FILE, expected);
  }

  @Test
  public final void testPeopleLookingUpByHeaderRefsAndIndexesShouldPopulateValuesAccordingly() {
    List<PersonFullnameHeaderNamesAndIndexes> expected =
        Arrays.asList(
            new PersonFullnameHeaderNamesAndIndexes("Chuck", "Albert", "Smith"),
            new PersonFullnameHeaderNamesAndIndexes("Bruce", null, "Johnson"),
            new PersonFullnameHeaderNamesAndIndexes("Michael", "Davis", "Jones"));

    assertStreamProducesResult(
        PersonFullnameHeaderNamesAndIndexes.class, FileToTest.PERSON_NAMES_FILE, expected);
  }

  @Test
  public final void
      testPeopleLookingUpByHeaderNamesAndRefsAndIndexesShouldPopulateValuesAccordingly() {
    List<PersonFullnameWithHeaderNamesAndRefsAndIndex> expected =
        Arrays.asList(
            new PersonFullnameWithHeaderNamesAndRefsAndIndex("Chuck", "Albert", "Smith"),
            new PersonFullnameWithHeaderNamesAndRefsAndIndex("Bruce", null, "Johnson"),
            new PersonFullnameWithHeaderNamesAndRefsAndIndex("Michael", "Davis", "Jones"));

    assertStreamProducesResult(
        PersonFullnameWithHeaderNamesAndRefsAndIndex.class, FileToTest.PERSON_NAMES_FILE, expected);
  }

  @Test
  public final void testPeopleLookingUpWithoutSpecifyingHeaderNameShouldUseTheFieldName() {
    List<PersonFullnameHeaderNoName> expected =
        Arrays.asList(
            new PersonFullnameHeaderNoName("Chuck", "Albert", "Smith"),
            new PersonFullnameHeaderNoName("Bruce", null, "Johnson"),
            new PersonFullnameHeaderNoName("Michael", "Davis", "Jones"));

    assertStreamProducesResult(
        PersonFullnameHeaderNoName.class, FileToTest.PERSON_NAMES_FILE, expected);
  }

  @Test
  public final void
      testPeopleWithIncompleteRowsLookingUpByHeaderNameShouldPopulateValuesAvailable() {
    List<PersonFullnameHeaderNames> expected =
        Arrays.asList(
            new PersonFullnameHeaderNames("Chuck", "Albert", null),
            new PersonFullnameHeaderNames("Bruce", null, null),
            new PersonFullnameHeaderNames("Michael", "Davis", null));

    assertStreamProducesResult(
        PersonFullnameHeaderNames.class, FileToTest.PERSON_NAMES_INCOMPLETE_FILE, expected);
  }

  @Test
  public final void
      testPeopleWithIncompleteRowsLookingUpByHeaderIndexesShouldPopulateValuesAvailable() {
    List<PersonFullnameHeaderIndexes> expected =
        Arrays.asList(
            new PersonFullnameHeaderIndexes("Chuck", "Albert", null),
            new PersonFullnameHeaderIndexes("Bruce", null, null),
            new PersonFullnameHeaderIndexes("Michael", "Davis", null));

    assertStreamProducesResult(
        PersonFullnameHeaderIndexes.class, FileToTest.PERSON_NAMES_INCOMPLETE_FILE, expected);
  }

  @Test
  public final void testPeopleUsingCustomAdapterFieldsShouldFollowCustomAdapterRules() {
    List<PersonFullnameHeaderNamesCustomAdapter> expected =
        Arrays.asList(
            new PersonFullnameHeaderNamesCustomAdapter("Mr/Ms Chuck", 'A', "Smith"),
            new PersonFullnameHeaderNamesCustomAdapter("Mr/Ms Bruce", '\0', "Johnson"),
            new PersonFullnameHeaderNamesCustomAdapter("Mr/Ms Michael", 'D', "Jones"));

    assertStreamProducesResult(
        PersonFullnameHeaderNamesCustomAdapter.class,
        FileToTest.PERSON_NAMES_AND_DOB_FILE,
        expected);
  }

  @Test(expected = IllegalArgumentException.class)
  public final void testPeopleUsingIllegalCustomAdapterTypeShouldThrowException() {
    streamToList(
        PersonFullnameHeaderNamesIllegalCustomAdapterType.class, FileToTest.PERSON_FIRST_NAMES);
  }

  @Test
  public final void
      testPeopleLookingUpByHeaderNamesWithNonExistentHeaderShouldLogWarningAndIgnore() {
    // ages should be all zero because an "age" header does not exist
    List<PersonAge> expected =
        Arrays.asList(
            new PersonAge("Chuck", 0), new PersonAge("Bruce", 0), new PersonAge("Michael", 0));

    assertStreamProducesResult(PersonAge.class, FileToTest.PERSON_NAMES_FILE, expected);
  }

  @Test
  public final void testPeopleLookingUpByHeaderIndexesOutOfBoundsShouldIgnore() {
    List<PersonFullnameHeaderIndexes> expected =
        Arrays.asList(
            new PersonFullnameHeaderIndexes("Chuck", null, null),
            new PersonFullnameHeaderIndexes("Bruce", null, null),
            new PersonFullnameHeaderIndexes("Michael", null, null));

    assertStreamProducesResult(
        PersonFullnameHeaderIndexes.class, FileToTest.PERSON_FIRST_NAMES, expected);
  }

  @Test
  public final void testPeopleUsingStringAndIntFieldsShouldCallBuiltinAdaptersCorrectly() {
    List<PersonAge> expected =
        Arrays.asList(
            new PersonAge("Chuck", 77), new PersonAge("Bruce", 76), new PersonAge("Michael", 58));

    assertStreamProducesResult(PersonAge.class, FileToTest.PERSON_AGES_FILE, expected);
  }

  @Test
  public final void testPeopleNamesSkippingOneRowShouldIgnoreThatRow() {
    List<PersonFullnameHeaderNames> expected =
        Arrays.asList(
            new PersonFullnameHeaderNames("Chuck", "Albert", "Smith"),
            new PersonFullnameHeaderNames("Michael", "Davis", "Jones"));

    assertStreamProducesResult(
        PersonFullnameHeaderNames.class, FileToTest.PERSON_NAMES_SKIP_ONE_ROW, expected);
  }

  @Test
  public final void testPeopleNamesShiftedRightShouldPopulateValuesCorrectly() {
    List<PersonFullnameHeaderNames> expected =
        Arrays.asList(
            new PersonFullnameHeaderNames("Chuck", "Albert", "Smith"),
            new PersonFullnameHeaderNames("Bruce", null, "Johnson"),
            new PersonFullnameHeaderNames("Michael", "Davis", "Jones"));

    assertStreamProducesResult(
        PersonFullnameHeaderNames.class, FileToTest.PERSON_NAMES_SHIFTED_RIGHT, expected);
  }

  @Test
  public final void testPeopleNamesShiftedDownShouldPopulateValuesCorrectly() {
    List<PersonFullnameHeaderNames> expected =
        Arrays.asList(
            new PersonFullnameHeaderNames("Chuck", "Albert", "Smith"),
            new PersonFullnameHeaderNames("Bruce", null, "Johnson"),
            new PersonFullnameHeaderNames("Michael", "Davis", "Jones"));

    assertStreamProducesResult(
        PersonFullnameHeaderNames.class, FileToTest.PERSON_NAMES_SHIFTED_DOWN, expected);
  }

  @Test
  public final void testPeopleNamesShiftedRightAndDownShouldPopulateValuesCorrectly() {
    List<PersonFullnameHeaderNames> expected =
        Arrays.asList(
            new PersonFullnameHeaderNames("Chuck", "Albert", "Smith"),
            new PersonFullnameHeaderNames("Bruce", null, "Johnson"),
            new PersonFullnameHeaderNames("Michael", "Davis", "Jones"));

    assertStreamProducesResult(
        PersonFullnameHeaderNames.class, FileToTest.PERSON_NAMES_SHIFTED_RIGHT_AND_DOWN, expected);
  }

  @Test
  public final void
      testPeopleNamesAllEmptyExceptAdditionalUnmappedColumnsShouldReturnBeanWithAllUnsetValues() {
    List<PersonFullnameHeaderNames> expected =
        Arrays.asList(
            new PersonFullnameHeaderNames(null, null, null),
            new PersonFullnameHeaderNames(null, null, null),
            new PersonFullnameHeaderNames(null, null, null));

    assertStreamProducesResult(
        PersonFullnameHeaderNames.class, FileToTest.PERSON_NAMES_ONLY_DOB_COMPLETE, expected);
  }

  @Test
  public final void testPeopleNamesAllEmptyExceptMiddleNameShouldPopulateValuesCorrectly() {
    List<PersonFullnameHeaderNames> expected =
        Arrays.asList(
            new PersonFullnameHeaderNames(null, "Albert", null),
            new PersonFullnameHeaderNames(null, "Davis", null));

    assertStreamProducesResult(
        PersonFullnameHeaderNames.class,
        FileToTest.PERSON_NAMES_ONLY_MIDDLENAME_COMPLETE,
        expected);
  }

  @Test
  public final void testConvertingArrayOfIntegers() {
    List<ArrayOfIntegers> expected =
        Arrays.asList(new ArrayOfIntegers(new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}));

    assertStreamProducesResult(ArrayOfIntegers.class, FileToTest.NUMBERS_ARRAY, expected);
  }

  @Test
  public final void testConvertingArrayOfInts() {
    List<ArrayOfInts> expected =
        Arrays.asList(new ArrayOfInts(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}));

    assertStreamProducesResult(ArrayOfInts.class, FileToTest.NUMBERS_ARRAY, expected);
  }

  @Test
  public final void testConvertingArrayOfBigIntegers() {
    List<ArrayOfBigIntegers> expected =
        Arrays.asList(
            new ArrayOfBigIntegers(
                new BigInteger[] {
                  new BigInteger("0"),
                  new BigInteger("1"),
                  new BigInteger("2"),
                  new BigInteger("3"),
                  new BigInteger("4"),
                  new BigInteger("5"),
                  new BigInteger("6"),
                  new BigInteger("7"),
                  new BigInteger("8"),
                  new BigInteger("9")
                }));

    assertStreamProducesResult(ArrayOfBigIntegers.class, FileToTest.NUMBERS_ARRAY, expected);
  }

  @Test
  public final void testConvertingArrayOfStrings() {
    List<ArrayOfStrings> expected =
        Arrays.asList(
            new ArrayOfStrings(new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"}));

    assertStreamProducesResult(ArrayOfStrings.class, FileToTest.NUMBERS_ARRAY, expected);
  }

  @Test
  public final void testConvertingListOfIntegers() {
    List<ListOfIntegers> expected =
        Arrays.asList(new ListOfIntegers(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));

    assertStreamProducesResult(ListOfIntegers.class, FileToTest.NUMBERS_ARRAY, expected);
  }

  @Test
  public final void testConvertingArrayListOfIntegers() {
    List<ArrayListOfIntegers> expected =
        Arrays.asList(
            new ArrayListOfIntegers(
                IntStream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
                    .boxed()
                    .collect(Collectors.toCollection(ArrayList::new))));

    assertStreamProducesResult(ArrayListOfIntegers.class, FileToTest.NUMBERS_ARRAY, expected);
  }

  @Test
  public final void testConvertingListOfStrings() {
    List<ListOfStrings> expected =
        Arrays.asList(
            new ListOfStrings(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")));

    assertStreamProducesResult(ListOfStrings.class, FileToTest.NUMBERS_ARRAY, expected);
  }

  @Test
  public final void testConvertingArrayListOfStrings() {
    List<ArrayListOfStrings> expected =
        Arrays.asList(
            new ArrayListOfStrings(
                Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
                    .stream()
                    .collect(Collectors.toCollection(ArrayList<String>::new))));

    assertStreamProducesResult(ArrayListOfStrings.class, FileToTest.NUMBERS_ARRAY, expected);
  }

  @Test(expected = UnsupportedAdapterFieldException.class)
  public final void testPeopleWithUnsupportedFieldShouldResultInException() {
    streamToList(PersonUnsupportedAdapter.class, FileToTest.PERSON_FIRST_NAMES);
  }

  @Test(expected = CellAdapterException.class)
  public final void testPeopleUsingStringAndInvalidIntFieldsShouldThrowException() {
    streamToList(PersonAgeThrowingExceptionIfInvalidAge.class, FileToTest.PERSON_AGES_INVALID_FILE);
  }

  @Test
  public final void
      testPeopleUsingStringAndInvalidIntFieldsSuppressingExceptionShouldNotThrowException() {
    List<PersonAge> expected =
        Arrays.asList(
            new PersonAge("Chuck", 77), new PersonAge("Bruce", 0), new PersonAge("Michael", 58));

    assertStreamProducesResult(PersonAge.class, FileToTest.PERSON_AGES_INVALID_FILE, expected);
  }

  @Test(expected = IllegalHeaderException.class)
  public final void testPeopleIllegalHeaderSpecifyingBothNameAndIndexShouldThrowException() {
    streamToList(PersonFullnameIllegalHeaderNameAndIndex.class, FileToTest.PERSON_NAMES_FILE);
  }

  @Test(expected = IllegalHeaderException.class)
  public final void testPeopleIllegalHeaderSpecifyingBothNameAndRefShouldThrowException() {
    streamToList(PersonFullnameIllegalHeaderNameAndRef.class, FileToTest.PERSON_NAMES_FILE);
  }

  @Test(expected = IllegalHeaderException.class)
  public final void testPeopleIllegalHeaderSpecifyingBothIndexAndRefShouldThrowException() {
    streamToList(PersonFullnameIllegalHeaderIndexAndRef.class, FileToTest.PERSON_NAMES_FILE);
  }

  @Test(expected = IllegalHeaderException.class)
  public final void testPeopleIllegalHeaderSpecifyingBothNameAndRefAndIndexShouldThrowException() {
    streamToList(PersonFullnameIllegalHeaderNameAndRefAndIndex.class, FileToTest.PERSON_NAMES_FILE);
  }

  @Test(expected = IllegalHeaderException.class)
  public final void testPeopleInvalidNameShouldThrowException() {
    streamToList(PersonFullnameIllegalHeaderNameAndRefAndIndex.class, FileToTest.PERSON_NAMES_FILE);
  }

  @Test(expected = IllegalHeaderException.class)
  public final void testPeopleInvalidRefShouldThrowException() {
    streamToList(PersonFullnameIllegalHeaderNameAndRefAndIndex.class, FileToTest.PERSON_NAMES_FILE);
  }

  @Test(expected = IllegalHeaderException.class)
  public final void testPeopleInvalidIndexShouldThrowException() {
    streamToList(PersonFullnameIllegalHeaderNameAndRefAndIndex.class, FileToTest.PERSON_NAMES_FILE);
  }

  @Test
  public final void testPeopleFileContainingOnlyHeadersShouldReturnEmptyStream() {
    assertStreamProducesResult(
        PersonFullnameHeaderNames.class, FileToTest.PERSON_NAMES_ONLY_HEADERS, new ArrayList<>());
  }

  @Test
  public final void testEmptyPeopleFileShouldReturnEmptyStream() {
    assertStreamProducesResult(
        PersonFullnameHeaderNames.class, FileToTest.EMPTY_FILE, new ArrayList<>());
  }

  @Test
  public final void testReadingMultipleTimesWithTheSameContextShouldHaveNoIssues() {
    String filepath = FileToTest.PERSON_FIRST_NAMES.path(extension()).toString();

    JEXMContext<PersonFirstName> jexmContext = JEXMContext.newInstance(PersonFirstName.class);
    long count1, count2;

    try (Stream<PersonFirstName> s = jexmContext.read(filepath)) {
      count1 = s.count();
    }

    try (Stream<PersonFirstName> s = jexmContext.read(filepath)) {
      count2 = s.count();
    }

    assertEquals(count1, count2);
  }
}
