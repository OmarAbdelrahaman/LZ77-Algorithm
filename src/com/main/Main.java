package com.main;

import tag.Tag;

import java.util.ArrayList;
import java.util.Scanner;
import java.lang.String;

public class Main {

    static int isFound(String txt, String target, int pointer, int searchBuffer) {
        int found = -1;
        // search about the target between the pointer and the search buffer
        for (int i = Math.max(0, pointer - searchBuffer); i <= pointer - target.length(); ++i) {
            if (target.length() > 0 && target.charAt(0) == txt.charAt(i)) {
                int f = 0;
                for (int j = i, k = 0; k < target.length(); ++k, ++j) {
                    if (target.charAt(k) == txt.charAt(j))
                        f++;
                    else
                        break;
                }
                if (f == target.length())
                    found = pointer - i;
            }
        }
        /// if found return how many steps we will back
        ///  else return -1
        return found;

    }


    /// to calculate number of bits
    static int NumOfBits(int x) {
        if (x == 0)
            return 1;
        int ans = 0;
        while (x > 0) {
            x /= 2;
            ans++;
        }
        return ans;
    }


    static void Compression(String txt, int searchBuffer, int lookAheadBuffer) {
        System.out.println("-------------------------------------");
        ArrayList<Tag> tags = new ArrayList<>();
        int txtSize = txt.length();
        String target = "";
        ///   search buffer <- pointer -> look ahead buffer
        for (int pointer = 0; pointer < txtSize; ++pointer) {
            target += txt.charAt(pointer);
            int found = isFound(txt, target, pointer + 1 - target.length(), searchBuffer);

            /// we will add new tag if we can't find the target string or reach the look ahead buffer size
            if (found == -1 || target.length() == lookAheadBuffer) {
                String lastTarget = target.substring(0, target.length() - 1);
                int lastFound = isFound(txt, lastTarget, pointer - lastTarget.length(), searchBuffer);
                Tag tag = new Tag(((lastFound == -1) ? 0 : lastFound), target.length() - 1, target.charAt((target.length() - 1)));
                tags.add(tag);
                target = "";
            }
        }
        /// to check that we took all the text
        if (target.length() != 0) {
            String lastTarget = target.substring(0, target.length() - 1);
            int lastFound = isFound(txt, lastTarget, txtSize - 1 - lastTarget.length(), searchBuffer);
            Tag tag = new Tag(((lastFound == -1) ? 0 : lastFound), target.length() - 1, target.charAt((target.length() - 1)));
            tags.add(tag);
        }

        int mxBacking = -1000000000, mxLen = -1000000000, mxChar = -1000000000;
        for (Tag tag : tags) {

            mxBacking = Math.max(mxBacking, tag.backing);
            mxLen = Math.max(mxLen, tag.len);
            mxChar = Math.max(mxChar, tag.nextChar);
            System.out.println("<" + tag.backing + ", " + tag.len + ", " + tag.nextChar + ">");
        }
        System.out.println("-------------------------------------");
        mxBacking = NumOfBits(mxBacking);
        mxLen = NumOfBits(mxLen);
        mxChar = NumOfBits(mxChar);
        int TagSize = mxBacking + mxLen + 8;
        // mxChar can be 8 byte to generalize
        System.out.println("The Size of the Tag = " + mxBacking + " + " + mxLen + " + 8 = " + TagSize + " Bits");
        System.out.println("THe Size After Compression = " + tags.size() * TagSize + " Bits");
        System.out.println("THe Size Before Compression = " + txtSize * 8 + " Bits");
    }


    static void Decompression(ArrayList<Tag> tags) {
        String decoding = "";
        int n = tags.size();
        for (int i = 0; i < n; i++) {
            int x = tags.get(i).backing;
            int y = tags.get(i).len;
            char z = tags.get(i).nextChar;
            int curr = decoding.length(); /// from where should start backing
            for (int j = curr - x; j < curr - x + y; ++j) {
                decoding += decoding.charAt(j);
            }
            // add the next symbol
            decoding += z;
        }

        System.out.println("Decompression: " + decoding);


    }


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("----------------------------");
        System.out.println("--- Welcome To LZ_77 App ---");
        System.out.println("----------------------------");

        while (true) {
            System.out.println("Choose Number From Our List To Start: ");
            System.out.println("-------------------------------------");
            System.out.println("1- Compression");
            System.out.println("2- Decompression");
            System.out.println("3- Exit");
            System.out.println("-------------------------------------");
            System.out.print(">> ");
            int choice = input.nextInt();
            System.out.println("-------------------------------------");

            if (choice == 1) {
                System.out.println("Enter Text To Compression: ");
                String txt = input.next();
                System.out.println("Enter Search Buffer Size:");
                int searchBuffer = input.nextInt();
                if(searchBuffer<=0)
                    searchBuffer=txt.length();
                System.out.println("Enter Look Ahead Buffer Size:");
                int lookAheadBuffer = input.nextInt();
                if(lookAheadBuffer<=0)
                    lookAheadBuffer=txt.length();

                Compression(txt, searchBuffer, lookAheadBuffer);
                System.out.println("-------------------------------------");
                System.out.println("-------------------------------------\n");


            } else if (choice == 2) {
                ArrayList<Tag> tags = new ArrayList<>();
                System.out.println("Enter Tags' Size: ");
                int numOfTags = input.nextInt();
                for (int i = 0; i < numOfTags; i++) {
                    int backing = input.nextInt();
                    int Len = input.nextInt();
                    char nextChar = input.next().charAt(0);
                    Tag tag = new Tag(backing, Len, nextChar);
                    tags.add(tag);
                }
                System.out.println("-------------------------------------");
                Decompression(tags);
                System.out.println("-------------------------------------");
                System.out.println("-------------------------------------\n");

            } else if (choice == 3) {
                System.out.println("Exit..");
                System.out.println("-------------------------------------");
                System.out.println("-------------------------------------\n");
                break;
            }
        }


    }
}
/**
  8
  0 0 a
  0 0 b
  2 1 a
  3 2 b
  5 3 b
  2 2 b
  5 5 b
  1 1 a
  abaababaabbbbbbbbbbbba
  ABAABABAABBBBBBBBBBBBA



  searchBuffer->5 lookAheadBuffer->5
  ababababbbbbbabababab
  8
  0 0 a
  0 0 b
  2 2 a
  4 3 b
  2 2 b
  1 1 a
  2 2 b
  3 3 b



  searchBuffer->20 lookAheadBuffer->3
  abababababababab
  7
  0 0 a
  0 0 b
  2 2 a
  2 2 b
  2 2 a
  2 2 b
  2 1 b



  searchBuffer->5 lookAheadBuffer->5
  ababababbbbbbabababab
  8
  0 0 a
  0 0 b
  2 2 a
  4 3 b
  2 2 b
  1 1 a
  2 2 b
  3 3 b



  searchBuffer->20 lookAheadBuffer->3
  abababababababab
  7
  0 0 a
  0 0 b
  2 2 a
  2 2 b
  2 2 a
  2 2 b
  2 1 b

 **/



/**
 searchBuffer->5 lookAheadBuffer->5
 ababababbbbbbabababab
 8
 0 0 a
 0 0 b
 2 2 a
 4 3 b
 2 2 b
 1 1 a
 2 2 b
 3 3 b
 **/



/**
 searchBuffer->20 lookAheadBuffer->3
 abababababababab
 7
 0 0 a
 0 0 b
 2 2 a
 2 2 b
 2 2 a
 2 2 b
 2 1 b
 **/