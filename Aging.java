
import java.util.*;
import java.io.*;
import java.util.Random; 
import java.io.FileWriter;
import java.io.IOException;

class Page 
{
    String pageNumber;
    int reference;

    Page(String pageNumber, int reference)
    {
        this.pageNumber = pageNumber;
        this.reference = reference;
    }

    public void setValue(int reference)
    {
        this.reference = reference;
    }

}//


public class Aging
{

    public static void generateFile(String fileName)
    {
        //Build an input stream
        Random rand = new Random(); 
        int upperbound = 20;
        StringBuilder inputStream = new StringBuilder();

        //write to a file
        try 
        {
            FileWriter fw = new FileWriter("./input.txt");
            BufferedWriter writer = new BufferedWriter(fw);
            for(int k = 0; k < 10000; k++)
            {
                int int_random = rand.nextInt(upperbound);
                writer.append(new String(int_random + ","));
            }//

            //Close file
            writer.close();
        }
        catch (Exception e) 
        {
            e.getStackTrace();
        } 
        
    }//

    public static String readFile(String filename)
    {
        //Base Checking
        if(filename == null)
        {
            return null;
        }

        //Result
        String result = null;

        //Read input file
        StringBuilder stream = new StringBuilder();
        try 
        {
            File file = new File(filename);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null)
            { 
                stream.append(st);
            }// while
        }
        catch (Exception e) 
        {
            e.getStackTrace();
        } 

        //Return
        result = stream.toString();
        return result;

    }//


    public static List<Integer> computePageFaults(String filename)
    {
        //Init
        int numberFrames = 10;
        List<Page> memory = new ArrayList<>();
        String inputStream = readFile(filename);
        String[] refSequence = inputStream.split(",");
        
        //Read reference stream from a file
        List<Integer> result = new ArrayList<>();

        //Access memory
        int counter = 0;

        for(int k = 0; k < refSequence.length; k++)
        {
            //Current page
            String pageNumber = refSequence[k];
            System.out.println("\n current page:" + pageNumber);

            //Access the page
            boolean flag = false; 
            for(Page page : memory)
            {
                if(page.pageNumber.equals(pageNumber))
                {
                    flag = true;
                }
            }//

            //Judge if page fault
            if(flag == false)
            {
                //Page fault
                counter++;

                //replace one page from memory
                if(memory.size() < numberFrames)
                {
                    memory.add(new Page(pageNumber, 0));
                    for(Page page : memory)
                    {
                        System.out.println(page.pageNumber + "," + page.reference);
                    }//
                    System.out.println();
                }
                else 
                {
                    
                    //Sort memory on reference in ascending order
                    Collections.sort(memory, (a, b) -> a.reference - b.reference);
                    System.out.println("\n SORT");
                    for(Page page : memory)
                    {
                        System.out.println(page.pageNumber + "," + page.reference);
                    }//

                    //Replace the minimum reference
                    memory.remove(0);
                    System.out.println("REMOVE");
                    for(Page page : memory)
                    {
                        System.out.println(page.pageNumber + "," + page.reference);
                    }//

                    //Add a new page to the memory
                    memory.add(new Page(pageNumber, 0));
                    System.out.println("ADD");
                    for(Page page : memory)
                    {
                        System.out.println(page.pageNumber + "," + page.reference);
                    }//

                    System.out.println();

                }// 
            }
            else 
            {
                System.out.println("HIT");
            }
            System.out.println();

            //Update memory
            for(Page page : memory)
            {
                int reference = page.reference;

                reference = reference >> 1;
                System.out.println("Pagenumber:" + page.pageNumber + ", reference " + reference);

                if(page.pageNumber.equals(pageNumber))
                {
                    reference += Math.pow(2, 7);
                }

                page.setValue(reference);
            }//

            System.out.println("Updated!");
            for(Page page : memory)
            {
                System.out.println(page.pageNumber + "," + page.reference);
            }//

            //Update result
            if((k % 1000) == 999)
            {
                result.add(counter);
                counter = 0;
            }// if

        }// for(int k = 0)

        //Return
        return result;
    }//


    //main
    public static void main(String[] args)
    {
        //Generate reference file
        String fileName = "./input.txt";
        Aging.generateFile(fileName);
        
        //Compute page faults
        List<Integer> result = Aging.computePageFaults(fileName);

        //Plot
        System.out.println(result);
    }

}//