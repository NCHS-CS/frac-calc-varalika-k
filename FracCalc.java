// Varalika Konduri
// Period 6
// December 17 2025
// Fraction Calculator Project 
// Evaluates mathematical expressions with fractions and mixed numbers.
// Outputs the result in reduced mixed number format.

import java.util.*;

// Evaluates arithemic expressions with fractions and mixed numbers.
// Using addition, subtraction, multiplication, and division.
public class FracCalc {

   // Scanner is used for reading user input from the console.
   public static Scanner console = new Scanner(System.in);

   // This main method will loop through user input and then call the
   // correct method to execute the user's request for help, test, or
   // the mathematical operation on fractions. or, quit.
   public static void main(String[] args) {

      // initialize to false so that we start our loop
      boolean done = false;

      // When the user types in "quit", we are done.
      while (!done) {
         // prompt the user for input
         String input = getInput();

         // special case the "quit" command
         if (input.equalsIgnoreCase("quit")) {
            done = true;
         } else if (!UnitTestRunner.processCommand(input, FracCalc::processCommand)) {
            // We allowed the UnitTestRunner to handle the command first.
            // If the UnitTestRunner didn't handled the command, process normally.
            String result = processCommand(input);

            // print the result of processing the command
            System.out.println(result);
         }
      }

      System.out.println("Goodbye!");
      console.close();
   }

   // Prompt the user with a simple, "Enter: " and get the line of input.
   // Return the full line that the user typed in.
   public static String getInput() {
      System.out.print("Enter: ");
      String input = console.nextLine();
      if (input.equalsIgnoreCase("quit")) {
         return "quit";
      } else {
         return input;
      }
   }

   // processCommand will process every user command except for "quit".
   // It will return the String that should be printed to the console.
   // This method won't print anything.
   public static String processCommand(String input) {

      if (input.equalsIgnoreCase("help")) {
         return provideHelp();
      }

      // if the command is not "help", it should be an expression.
      // Of course, this is only if the user is being nice.
      return processExpression(input);
   }

   // ProcessExpression will take in a mathematical expression
   // including two fractions and an operator.
   // The parameter input a string containg a fraction expression.
   // It will return the result of the expression as a String.
   
   public static String processExpression(String input) {
      String left = getLeftOperand(input); 
      String right = getRightOperand(input);
      String op = getOperator(input); 

      int whole1 = extractWhole(left); 
      int numerator1 = extractNumerator(left); 
      int denominator1 = extractDenominator(left); 

      int whole2 = extractWhole(right);
      int numerator2 = extractNumerator(right); 
      int denominator2 = extractDenominator(right); 

      int improperFraction1 = convertToImproperNumerator(whole1, numerator1, denominator1); // improper fraction of left operhand
      int improperFraction2 = convertToImproperNumerator(whole2, numerator2, denominator2); // improper fraction of right operhandquit

      int resultNumerator = 0; // numerator of the result
      int resultDenominator = 1; // denominator of the result

      if(op.equals("+")) {
         resultNumerator = improperFraction1 * denominator2 + improperFraction2 * denominator1;
         resultDenominator = denominator1 * denominator2;
      } else if(op.equals("-")) {
         resultNumerator = improperFraction1 * denominator2 - improperFraction2 * denominator1;
         resultDenominator = denominator1 * denominator2;
      } else if(op.equals("*")) {
         resultNumerator = improperFraction1 * improperFraction2;
         resultDenominator = denominator1 * denominator2;
      } else if(op.equals("/")) {
         if (improperFraction2 == 0 || denominator2 == 0) {
            throw new ArithmeticException("Division by zero is not allowed.");
         } else {
            resultNumerator = improperFraction1 * denominator2;
            resultDenominator = improperFraction2 * denominator1;
         }
      }

      // Reduce the fraction using greatest common divisor
      int gcd = getGCD(Math.abs(resultNumerator), Math.abs(resultDenominator)); // greatest common divisor of numerator and denominator
      resultNumerator /= gcd; 
      resultDenominator /= gcd;
      if (resultDenominator < 0) { 
         // Checks if the denominator is positive. 
         resultDenominator *= -1; 
         resultNumerator *= -1; 
         
      }
      return formatAnswer(resultNumerator, resultDenominator); // format and return the answer
   }

   // Helper methods to parse the input string
   // and extract the left operhand, right operhand, and operator.

   // Extracts and returns the left operand from an experssion.
   // The parameter inputs the full expession. 
   // It returns the left operand.
   public static String getLeftOperand(String input) {
      int space = input.indexOf(" ");
      return input.substring(0, space);
   }

   // Extracts and returns the right operand from an experssion.
   // The parameter inputs the full expession. 
   // It returns the right operand.
   public static String getRightOperand(String input) {
      int space = input.lastIndexOf(" ");
      return input.substring(space + 1);
   }

   // Extracts and returns the operator from an expression.
   // The parameter inputs the full expression. 
   // It returns the operator.
   public static String getOperator(String input) {
      int first = input.indexOf(" ");
      int second = input.indexOf(" ", first + 1);
      return input.substring(first + 1, second);
   }
   
   // Coverts a mixed number into an improper fraction.
   // Returns the whole number portion of an operand.
   // The parameter is the operhand which holds the mixed number.
   public static int extractWhole(String operhand) {
      int underscore = operhand.indexOf("_");
      int slash = operhand.indexOf("/");
      if (underscore != -1) {
         return Integer.parseInt(operhand.substring(0, underscore));
      } else if (slash == -1) {
         return Integer.parseInt(operhand);
      }
      return 0;
   }

   // This method returns the numerator of an operand.
   // The parameter is the operhand which holds the mixed number.
   // Returns the numerator value.
   public static int extractNumerator(String operhand) {
      int underscore = operhand.indexOf("_");
      int slash = operhand.indexOf("/");
      if (slash == -1) {
         return 0;
      }
      if (underscore == -1) {
         return Integer.parseInt(operhand.substring(0, slash));
      }
      return Integer.parseInt(operhand.substring(underscore + 1, slash));
   }

   // This method returns the denominator of an operand.
   // The parameter is the operhand which holds the mixed number.
   // Returns the denominator value.
   public static int extractDenominator(String operhand) {
      int slash = operhand.indexOf("/");
      if (slash == -1) {
         return 1;
      }
      return Integer.parseInt(operhand.substring(slash + 1));
   }

   // This method converts a mixed number to an improper fraction numerator.
   // It takes in the whole part, numerator, and denominator of the mixed number.
   // It returns the numerator of the improper fraction.
   public static int convertToImproperNumerator(int whole, int numerator, int denominator) {
      if (whole < 0) {
         return whole * denominator - numerator;
      }
      return whole * denominator + numerator;
   }

   // This method is useful for reducing fractions to their simplest form. 
   // Parameter a is the first integeter.
   // Parameter b is the second integer.
   // This returns the greatest common divisor of a and b.
   public static int getGCD(int a, int b) {
      while (b != 0) {
         int temporary = a % b;
         a = b;
         b = temporary;
      }
      return a;
   }

   // This method formats the answer as a string in mixed number format.
   // Parameter numerator is the numerator of the fraction.
   // Parameter denominator is the denominator of the fraction.
   // It returns the formatted answer as a string.
   public static String formatAnswer(int numerator, int denominator) {
      if (numerator == 0) {
         return "0";
      }
      int whole = numerator / denominator;
      int remainder = Math.abs(numerator % denominator); 
      if (remainder == 0) {
         return "" + whole;
      }
      if (whole == 0) {
         if (numerator < 0) {
            return "-" + remainder + "/" + denominator;
         }
         return remainder + "/" + denominator;
      }
      return whole + " " + remainder + "/" + denominator;
   }

   // Returns a string that is helpful to the user about how
   // to use the program. These are instructions to the user.
   // This method returns the help text as a string.
   public static String provideHelp() {
      // todo: Update this help text!

      String help = "Type in two numbers with an arithmetic operator like (+, -, /, *)\n";
      help += "Make sure to use spaces between the numbers and the operator.";

      return help;
   }
}
