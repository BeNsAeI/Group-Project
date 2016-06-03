/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.*;
import java.util.*;
import junit.framework.TestCase;

/**
 * Performs Validation Test for url validations.
 *
 * @version $Revision: 1128446 $ $Date: 2011-05-27 13:29:27 -0700 (Fri, 27 May 2011) $
 */
public class UrlValidatorTest extends TestCase {

   private boolean printStatus = false;
   private boolean printIndex = false;//print index that indicates current scheme,host,port,path, query test were using.

   public UrlValidatorTest(String testName) {
      super(testName);
   }

   
   
   public void testManualTest()
   {
	   UrlValidator urlVal = new UrlValidator(null, null, UrlValidator.ALLOW_ALL_SCHEMES);
	   System.out.println(urlVal.isValid("http://www.amazon.com"));
	   //Our own manual test
	   System.out.println(urlVal.isValid("http://www.google.com:80/test1?action=view true"));
	   System.out.println(urlVal.isValid("256.256.256.256:65636/test1?action=view"));
	   System.out.println(urlVal.isValid("h3t://go.cc:-1//test1//file?action=edit&mode=up"));
	   System.out.println(urlVal.isValid("?action=edit&mode=up"));
	   System.out.println(urlVal.isValid("http://255.com:80/"));
	   System.out.println(urlVal.isValid("http://go.au"));
	   
   }
   
   
  // Test input partition
   public void testScheme() {
	   UrlValidator urlVal = new UrlValidator(null, null, UrlValidator.ALLOW_ALL_SCHEMES);
	  
	   System.out.println("***************************These schemes should be valid.***************************");
	   
	   // begins with letter
	   System.out.println(urlVal.isValid("http://google.com"));
	   // contains letters, +, -, .
	   System.out.println(urlVal.isValid("h7+-.://google.com"));
	   // contains capital letters
	   System.out.println(urlVal.isValid("HTTP://google.com"));

	   
	   System.out.println("***************************These schemes should be invalid.***************************");
	   // doesn't begin with a letter
	   System.out.println(urlVal.isValid("7http://google.com"));
	   // contains invalid characters
	   System.out.println(urlVal.isValid("h7+-.[://google.com"));
   }
   
   public void testAuthority() {
	   UrlValidator urlVal = new UrlValidator(null, null, UrlValidator.ALLOW_ALL_SCHEMES);
	   
	   System.out.println("***************************These URLs should contain valid authority components.***************************");
	   
	   // begins with a double slash
	   System.out.println(urlVal.isValid("http://google.com"));
	   // port number between 0 and 65535
	   System.out.println(urlVal.isValid("http://www.google.com:80"));
	   // minimum port number
	   System.out.println(urlVal.isValid("http://www.google.com:0"));
	   // maximum port number
	   System.out.println(urlVal.isValid("http://www.google.com:65535"));
	   // valid username
	   System.out.println(urlVal.isValid("http://userName@www.google.com:80"));


	   System.out.println("***************************These URLs should contain invalid authority components.***************************");
	   
	   // doesn't start with double slash
	   System.out.println(urlVal.isValid("http:/google.com"));
	   // colon but no port number
	   System.out.println(urlVal.isValid("http://www.google.com:"));
	   // empty authority
	   System.out.println(urlVal.isValid("http://"));
	   
   }
   
   /*public void testPort() {
	   UrlValidator urlVal = new UrlValidator(null, null, UrlValidator.ALLOW_ALL_SCHEMES);
	   for (int i = 0; i < 2000; ++i) {
		   System.out.println(i + ": ");
		   System.out.println(urlVal.isValid("http://www.google.com:" + i));
	   }
   }*/

   public void testPath() {
	   UrlValidator urlVal = new UrlValidator(null, null, UrlValidator.ALLOW_ALL_SCHEMES);
	   
	   System.out.println("***************************These URLs should contain valid path components.***************************");

	   // empty path
	   System.out.println(urlVal.isValid("http://www.google.com"));
	   // nonempty path
	   System.out.println(urlVal.isValid("http://www.google.com/path1/path2"));
	   // using path segments
	   System.out.println(urlVal.isValid("http://www.google.com/./path1/path2"));
	   System.out.println(urlVal.isValid("http://www.google.com/../path1/path2"));

	   
	   System.out.println("***************************These URLs should contain invalid path components.***************************");

	   // empty authority
	   System.out.println(urlVal.isValid("http://path1/path2"));
	   // containing spaces
	   System.out.println(urlVal.isValid("http://www.google.com/path1 /path2"));
   }
   
   public void testQuery() {
	   UrlValidator urlVal = new UrlValidator(null, null, UrlValidator.ALLOW_ALL_SCHEMES);
	   
	   System.out.println("***************************These URLs should contain valid query components.***************************");

	   // empty query
	   System.out.println(urlVal.isValid("http://www.google.com"));
	   // single query
	   System.out.println(urlVal.isValid("http://www.google.com?action=view"));
	   // single query with path
	   System.out.println(urlVal.isValid("http://www.google.com/test1?action=view"));
	   // multiple queries delimited by ampersand
	   System.out.println(urlVal.isValid("http://www.google.com/test1?key1=value1&key2=value2"));
	   // multiple queries delimited by semicolon
	   System.out.println(urlVal.isValid("http://www.google.com/test1?key1=value1;key2=value2"));
	   // terminated by #
	   System.out.println(urlVal.isValid("http://www.google.com/test1?action=view#"));
	   // URL ends in fragment
	   System.out.println(urlVal.isValid("http://www.google.com/test1?action=view#top"));
	   // URL ends with question mark but without a query string
	   System.out.println(urlVal.isValid("http://www.google.com/test1?"));
	   
	   System.out.println("***************************These URLs should contain invalid query components.***************************");

	   // space in URL
	   System.out.println(urlVal.isValid("http://www.google.com/test1?action= view"));



   }
   
   public void testFragment() {
	   UrlValidator urlVal = new UrlValidator(null, null, UrlValidator.ALLOW_ALL_SCHEMES);
	   
	   System.out.println("***************************These URLs should contain valid fragment components.***************************");
	   
	   // fragment contains lowercase letters
	   System.out.println(urlVal.isValid("http://www.google.com/test1#top"));
	   // fragment contains uppercase letters
	   System.out.println(urlVal.isValid("http://www.google.com/test1#TOP"));
	   // URL terminated by #
	   System.out.println(urlVal.isValid("http://www.google.com/test1#"));
	   // fragment contains numbers 
	   System.out.println(urlVal.isValid("http://www.google.com/test1#top4u3"));
	   // fragment contains numbers and special characters / and ?
	   System.out.println(urlVal.isValid("http://www.google.com/test1#top/4u?3"));
	   // fragment contains other characters
	   System.out.println(urlVal.isValid("http://www.google.com/test1#top/4u?3[3"));

	   
	   System.out.println("***************************These URLs should contain invalid fragment components.***************************");

	   // fragment contains spaces
	   System.out.println(urlVal.isValid("http://www.google.com/test1#to p"));

   }
   
   public void testOtherPartitions() {
	   UrlValidator urlVal = new UrlValidator(null, null, UrlValidator.ALLOW_ALL_SCHEMES);
	   System.out.println(urlVal.isValid(""));
   }
     
   public void testIsValid()
   {
	   boolean result;
	   int numbBugs = 0;
	   int totalTest = 0;
	   ArrayList<String> tempStorage = new ArrayList<String>();
	   UrlValidator validURL = new UrlValidator();
	   
	   // Index for-loop
	   int iS, iA, iPo, iPa, iPao, iQ;
	   
	   // List of valid URL components
	   String[] validScheme = {"http://", "ftp://"};
	   String[] validAuthority = {"www.google.com", "go.com", "go.au", "0.0.0.0", "255.255.255.255", "255.com", "go.cc" };
	   String[] validPort = { ":80", ":65535", ":0", "", ":65636" };
	   String[] validPath = { "/test1", "/t123", "/$23", "", "/test1/file" };
	   String[] validPathOptions = { "/test1", "/t123", "/$23", "/test1/", "", "/test1/file", "/t123/file", "/$23/file"};
	   String[] validQuery = { "?action=view", "?action=edit&mode=up", ""};
	   
	   for (iS = 0; iS < validScheme.length; iS++) {
		   for (iA = 0; iA < validAuthority.length; iA++) {
			   for (iPo = 0; iPo < validPort.length; iPo++) {
				   for (iPa = 0; iPa < validPath.length; iPa++) {
					   for (iPao = 0; iPao < validPathOptions.length; iPao++) {
						   for (iQ = 0; iQ < validQuery.length; iQ++) {
							   totalTest+=1;
							   String newURL = validScheme[iS] + validAuthority[iA] + validPort[iPo] + validPath[iPa]
									   + validPathOptions[iPao] + validQuery[iQ];
							   //System.out.println(newURL);
							   result = validURL.isValid(newURL);
							   if (result == false){
								   numbBugs += 1;
								   tempStorage.add(newURL);
							   }
						   }
					   }
				   }
			   }
		   }
	   }
	   
	   System.out.println("**************** ALL VALID URLs ****************");
	   System.out.println("");
	   System.out.println("Number of Bugs: " + numbBugs);
	   System.out.println("Number of Total Test: " + totalTest);
	   System.out.println("");
	   //System.out.println(tempStorage);
	   
	   numbBugs = 0;
	   totalTest = 0;
	   tempStorage.clear();
	   //System.out.println(tempStorage);
	   
	   // List of invalid URL components
	   String[] invalidScheme = { "3ht://", "http:", "http:/", "://", "http/" };
	   String[] invalidAuthority = { "256.256.256.256", "1.2.3.4.5",  "1.2.3.4.","1.2.3", ".1.2.3.4", "go.a", "go.a1a", "go.1aa", "aaa.", ".aaa", "aaa", "" };
	   String[] invalidPort = { ":-1", ":65a" };
	   String[] invalidPath = { "/..", "/../", "/..//file", "/test1//file" };
	   String[] invalidPathOptions = { "/..", "/../", "/#", "/../file", "/..//file","/#/file" };
	   
	   for (iS = 0; iS < invalidScheme.length; iS++) {
		   for (iA = 0; iA < invalidAuthority.length; iA++) {
			   for (iPo = 0; iPo < invalidPort.length; iPo++) {
				   for (iPa = 0; iPa < invalidPath.length; iPa++) {
					   for (iPao = 0; iPao < invalidPathOptions.length; iPao++) {
						   for (iQ = 0; iQ < validQuery.length; iQ++) {
							   totalTest+=1;
							   String newURL = invalidScheme[iS] + invalidAuthority[iA] + invalidPort[iPo] + invalidPath[iPa]
									   + invalidPathOptions[iPao] + validQuery[iQ];
							   //System.out.println(newURL);
							   result = validURL.isValid(newURL);
							   if (result == true){
								   numbBugs += 1;
								   tempStorage.add(newURL);
							   }
						   }
					   }
				   }
			   }
		   }
	   }
	   
	   System.out.println("**************** ALL INVALID URLs TEST ****************");
	   System.out.println("");
	   System.out.println("Number of Bugs: " + numbBugs);
	   System.out.println("Number of Total Test: " + totalTest);
	
   }   
	      
   public void testAnyOtherUnitTest()
   {
	   
   }
   /**
    * Create set of tests by taking the testUrlXXX arrays and
    * running through all possible permutations of their combinations.
    *
    * @param testObjects Used to create a url.
    */  
}
