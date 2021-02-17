
package ex3_OPS;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MainTrain {
    static int[][] dataTable;
    static int size;
    static String FileName = "input2.txt";
    private static Object ArrayList;

    public static void readData(String FileName)
    {
        try
        {
            //parsing a CSV file into BufferedReader class constructor
            String line =  null;
            BufferedReader br = new BufferedReader(new FileReader(FileName));
            if((line = br.readLine()) != null){
                //String[] s = line.split(","); // First line - Names of cols
                size = Integer.parseInt(line);
                dataTable = new int[size][2];
            }
            for(int i = 0; i < size; i++)
            {
                if((line = br.readLine()) != null) { //run until the end of the file
                    String[] currentLine = line.split(",");
                    dataTable[i][0] = Integer.parseInt(currentLine[0]);
                    dataTable[i][1] = Integer.parseInt(currentLine[1]);
//                    System.out.println(dataTable[i][0] +" "+ dataTable[i][1] );
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public  static void sortArray() {
        for (int i = 0; i < dataTable.length - 1; i++) {
            for (int j = i + 1; j < dataTable.length; j++) {
                if (dataTable[j][0] < dataTable[i][0]) {
                    int temp[] = new int[2];
                    temp = dataTable[i];
                    dataTable[i] = dataTable[j];
                    dataTable[j] = temp;
                }
            }
        }
//        for(int i = 0; i< dataTable.length; i++){
//            System.out.println(dataTable[i][0]+" "+ dataTable[i][1]);
//        }
    }


    // EX1
    public  static void FCFS() // First come first serve
    {

        sortArray();

        int arrival_time = 0;
        int run_time = 0;
        int waiting_time = 0;
        int total_time = 0;

        float finish = 0;

        for (int i = 0; i < dataTable.length; i++){
            arrival_time = dataTable[i][0];
            run_time = dataTable[i][1];

            if ((i == 0) || (total_time < arrival_time)){
                waiting_time = 0;
            } else {
                waiting_time =  total_time - arrival_time;
            }

            total_time = arrival_time + run_time + waiting_time;

            finish += (run_time + waiting_time);
        }
        System.out.println("FCFS: mean turnaround = "+ (finish/dataTable.length));
    }

    public static void ReversMatrixToArrayList(ArrayList<int[]> tempList)
    {
        int size = dataTable.length - 1;

        for(int i=1;i <= size ;i++) {
            if(dataTable[i][1] != 0)
                tempList.add(dataTable[i]);
        }
    }

    // EX2
    public static void LCFS_NotPreemptive_TA()
    {
//        System.out.println(dataTable[0][0]+" "+dataTable[0][1]+" "+ (dataTable[0][0]+dataTable[0][1]));
        double total_time = dataTable[0][1];
        ArrayList<int[]> tempList =new ArrayList<int[]>();
        ReversMatrixToArrayList(tempList);
        int temp_FinishTime = dataTable[0][0]+dataTable[0][1];
        int waiting_time;

        while(tempList.size()!= 0)
        {
            for( int index = 0; index < tempList.size() ; index++)
            {
                int arrival_time = tempList.get(index)[0];
                int run_time = tempList.get(index)[1];

                if((temp_FinishTime <= arrival_time) || ( index == tempList.size()-1))
                {
                    if(temp_FinishTime>arrival_time)
                        waiting_time = temp_FinishTime - arrival_time;

                    else
                        waiting_time = 0;

                    temp_FinishTime = arrival_time+run_time + waiting_time;
                    total_time += (temp_FinishTime - arrival_time);
//                    System.out.println(tempList.get(index)[0] +" " +tempList.get(index)[1]+" "+temp_FinishTime);
                    tempList.remove(index);
                }
            }
        }
        System.out.println("LCFS (NP): mean turnaround = "+ (total_time/ dataTable.length ));
    }

    // EX3
    public static void LCFS_Preemptive_TA()
    {
        sortArray();

        float waiting_for_pre = dataTable[dataTable.length-1][1];
        float total_time = waiting_for_pre;
        float tmp = 0;

        if(dataTable.length == 2)
        {
            if (dataTable[0][0] + dataTable[0][1] < dataTable[1][0])
                total_time = dataTable[0][1] + dataTable[1][1];
            else
                total_time = dataTable[0][1] + dataTable[1][1] + dataTable[1][1];
        }
        else
        {
            for (int i = dataTable.length-2; i >= 0; i--) {

                if (dataTable[i][0] + dataTable[i][1] < dataTable[i + 1][0]) {
                    if (dataTable[i][1] == 0) {
                        continue;
                    } else {
                        total_time += dataTable[i + 1][1];
                        continue;
                    }
                }


                tmp = waiting_for_pre + dataTable[i][1];

                waiting_for_pre = tmp;

                total_time += tmp;
            }
        }

        System.out.println("LCFS (P): mean turnaround = "+ (total_time/dataTable.length));
    }

    // EX4
    public static void FromMatrixToArrayList(ArrayList<int[]> tempList) {
        int[][] temp = new int[dataTable.length][2];
        int size = dataTable.length - 1;
        for (int i = 0; i <= size; i++)
        {
            temp[i][0]= dataTable[i][0];
            temp[i][1]= dataTable[i][1];
        }
        for(int i=0;i <= size ;i++) {
            if(temp[i][1] != 0)
                tempList.add(temp[i]);
        }
    }
    public static int CalculateAT()
    {
        int size = dataTable.length - 1;
        int sum = 0;
        for(int i=0;i <= size ;i++)
            sum += dataTable[i][0];
        return sum;
    }

    public static void RR_TA()
    {
        int run_time;
        ArrayList<int[]> tmpList =new ArrayList<int[]>();
        FromMatrixToArrayList(tmpList);
        int timer = tmpList.get(0)[0];
        int totalArivedTime= CalculateAT();
        int waiting_time;
        double finish = 0;

        while(tmpList.size() != 0)
        {
            for (int i= 0; i < tmpList.size(); i++)
            {
                // if the first process in the list wasn't arrived yet
                if (timer < tmpList.get(0)[0])
                    timer = tmpList.get(0)[0];
                // if the current process is relevant to this session
                if(timer >= tmpList.get(i)[0])
                {
                    if(timer > tmpList.get(i)[0])
                        waiting_time = timer - tmpList.get(i)[0];
                    else
                        waiting_time = 0;
                    // If the process need to run more than 2
                    if(tmpList.get(i)[1]>= 2)
                    {
                        tmpList.get(i)[1] = tmpList.get(i)[1]-2;
                        tmpList.get(i)[0] += (waiting_time + 2 );
                        timer+=2;
                    }
                    else if(tmpList.get(i)[1] == 1)
                    {
                        tmpList.get(i)[1] = tmpList.get(i)[1]-1;
                        tmpList.get(i)[0] += (waiting_time + 1 );
                        timer+=1;
                    }
                    if(tmpList.get(i)[1] == 0)
                    {
                        finish += tmpList.get(i)[0];
                        tmpList.remove(i);
                        i--;
                    }
                }
            }
        }
        System.out.println("RR: mean turnaround  = "+ (finish - totalArivedTime) / dataTable.length);
    }


    //Ex 5
    public static ArrayList<int[]> sortByRunTime(int[][] Table)
    {
        ArrayList<int[]> tmpList =new ArrayList<int[]>();
        for(int i = 0; i < (Table.length-1); i++)
        {
            for(int j=i+1; j < Table.length; j++)
            {
                if(Table[i][1] > Table[j][1])
                {
                    int[] temp = new int[2];
                    temp =Table[i];
                    Table[i] = Table[j];
                    Table[j] = temp;
                }
            }
        }
        for(int i = 0; i < Table.length; i++)
        {
            if(Table[i][1]!=0)
                tmpList.add(Table[i]);
        }
        return tmpList;
    }

    public static int MinProcessInList(int timer ,  ArrayList<int[]> tmpList)
    {
        int min_runTime = 9999;
        int min_Index = 0;
        for(int i = 0; i < tmpList.size(); i++)
        {
            if((tmpList.get(i)[0]<= timer)&&(tmpList.get(i)[1] < min_runTime))
            {
                min_runTime = tmpList.get(i)[1];
                min_Index = i;
            }
        }
        return min_Index;
    }

    public static int FirstProcessInList(ArrayList<int[]> tmpList)
    {
        int minProcess = tmpList.get(0)[0];
        for(int i=1; i < tmpList.size(); i++)
        {
            if(tmpList.get(i)[0] < minProcess)
                minProcess = tmpList.get(i)[0];
        }
        return minProcess;
    }

    public static void SJF_TA()
    {
        int[][] temp = new int[dataTable.length][2];
        temp = dataTable;
        ArrayList<int[]> tempList2 = new ArrayList<int[]>();
        tempList2 = sortByRunTime(temp);
        double total_time =0;
        int waiting_time;
        int index;
        int temp_FinishTime = FirstProcessInList(tempList2);
        while(tempList2.size()!= 0)
        {
            index = MinProcessInList(temp_FinishTime,tempList2);
            if(temp_FinishTime > tempList2.get(index)[0])
                waiting_time = temp_FinishTime-tempList2.get(index)[0];
            else
                waiting_time = 0;

            tempList2.get(index)[1]--; //RunTime--
            tempList2.get(index)[0] += waiting_time+1;
            temp_FinishTime++;
            total_time+=(waiting_time + 1);
            if( tempList2.get(index)[1] == 0)
                tempList2.remove(index);
        }
        System.out.println("SJF: mean turnaround  = "+ (total_time)/ dataTable.length);
    }













    public static void main(String[] args)
    {

        System.out.println("\n======================== input 1 ========================");
        readData("input1.txt");
        FCFS();
        LCFS_NotPreemptive_TA();
        LCFS_Preemptive_TA();
        RR_TA();
        SJF_TA();
        System.out.println("\n");


        System.out.println("======================== input 2 ========================");
        readData("input2.txt");
        FCFS();
        LCFS_NotPreemptive_TA();
        LCFS_Preemptive_TA();
        RR_TA();
        SJF_TA();
        System.out.println("\n");


        System.out.println("======================== input 3 ========================");
        readData("input3.txt");
        FCFS();
        LCFS_NotPreemptive_TA();
        LCFS_Preemptive_TA();
        RR_TA();
        SJF_TA();
        System.out.println("\n");


        System.out.println("======================== input 4 ========================");
        readData("input4.txt");
        FCFS();
        LCFS_NotPreemptive_TA();
        LCFS_Preemptive_TA();
        RR_TA();
        SJF_TA();
        System.out.println("\n");


        System.out.println("======================== input 5 ========================");
        readData("input5.txt");
        FCFS();
        LCFS_NotPreemptive_TA();
        LCFS_Preemptive_TA();
        RR_TA();
        SJF_TA();
        System.out.println("\n");

    }


}

