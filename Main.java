import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        ArrayList<Double> list1 = new ArrayList<>();
        ArrayList<Integer> list2 = new ArrayList<>();
        ArrayList<String> list3 = new ArrayList<>();
        Double r; Integer c; String x;
        Double rr=0.0; //previous time

        System.out.println("Privét");

        do{
            x= scanner.nextLine();
            r = Double.valueOf(x.substring(0,x.indexOf(',')));
            //x = x.substring(x.indexOf(','));
            c = Integer.valueOf(x.substring(1+x.indexOf(','),x.length()-2));
            x = x.substring(x.length()-1);

            //System.out.print("enter real time:");
            //r= scanner.nextDouble();
            list1.add(r);
            //System.out.print("enter discrete time:");
            //c= scanner.nextInt();
            list2.add(c);
            //System.out.print("enter coin:");
            //x= scanner.nextLine(); x= scanner.nextLine();
            list3.add(x);
        }while(r >= 0);
        list1.remove(list1.size()-1);
        list2.remove(list2.size()-1);
        list3.remove(list3.size()-1);


        int q=0; int d=0; int n=0; int v=0; double e=0; double alarm1=999999999; int alarm2=0;
        boolean cc=false; boolean ccc = false; double rrr=0.0; String xx = "q";

        while(!list1.isEmpty())
        {
            if(list1.get(0)<alarm1 || (list1.get(0)==alarm1 && list2.get(0)<alarm2)) //external
            {
                r= list1.get(0); c= list2.get(0); x= list3.get(0);
                e = r-rr;
                //call delta external
                if(x.equals("q")){q++; v+=25;alarm1 = r + 2.0; alarm2=0;}
                if(x.equals("d")){d++; v+=10;alarm1 = r + 2.0; alarm2=0;}
                if(x.equals("n")){n++; v+=5;alarm1 = r + 2.0; alarm2=0;}
                if(x.equals("c")){
                    cc=true; alarm1 = r; alarm2++;
                    if(xx.equals("q")||xx.equals("d")||xx.equals("n")){rrr = rr;}
                }
                //alarm1 = r + 2.0; alarm2=0;
                list1.remove(0); list2.remove(0); list3.remove(0);
                rr=r; xx=x;
            }
            else if(list1.get(0)>alarm1 || (list1.get(0)==alarm1 && list2.get(0)>alarm2)) //internal
            {
                r=alarm1; c=alarm2;
                //e = r-rr;

                //lambda
                System.out.print("\n "+ r + "," + c + "    Λ i: ");

                if(cc){

                    System.out.print("c");
                    //delta
                    e = r-rrr;
                    cc=false;
                    alarm1 = r + 2.0 - e; alarm2=0;


                }else if(ccc){

                    System.out.print("c");
                    //delta
                    ccc=false;
                    alarm1 = 999999999; alarm2=0;

                }else{

                int poop = v / 100;
                for(int i=0;i<poop;i++){System.out.print("coffee ");}
                poop = v % 100;
                int qq=q; while(qq>0 && poop-25>=0){poop-=25; qq--; System.out.print("q");}
                int dd=d; while(dd>0 && poop-10>=0){poop-=10; dd--; System.out.print("d");}
                int nn=n; while(nn>0 && poop-5>=0){poop-=5; nn--; System.out.print("n");}
                if(poop>0) System.out.print(" and that's all i got ");

                //delta
                e= r-rr;
                poop = v % 100;
                while(q>0 && poop-25>=0){v-=25; q--; poop=v%100;}
                while(d>0 && poop-10>=0){v-=10; d--; poop=v%100;}
                while(n>0 && poop-5>=0){v-=5; n--; poop=v%100;}
                v=0;
                alarm1 = 999999999; alarm2 = 0;

                }rr=r; xx=x;
            }
            else //confluent
            {
                //poop (imperative)


                r= list1.get(0); c= list2.get(0); x= list3.get(0);
                e = r-rr;

                //lambda
                System.out.print("\n "+ r + "," + c + "    Λ c: ");


                //if(cc){System.out.print("c");} //not handled. just make not do deltalambda below.

                if(ccc){System.out.print("c");}
                ccc=false;


                int poop = v / 100;
                for(int i=0;i<poop;i++){System.out.print("coffee ");}
                poop = v % 100;
                int qq=q; while(qq>0 && poop-25>=0){poop-=25; qq--; System.out.print("q");}
                int dd=d; while(dd>0 && poop-10>=0){poop-=10; dd--; System.out.print("d");}
                int nn=n; while(nn>0 && poop-5>=0){poop-=5; nn--; System.out.print("n");}
                if(poop>0) System.out.print(" and that's all i got ");

                //delta
                poop = v % 100;
                while(q>0 && poop-25>=0){v-=25; q--; poop=v%100;}
                while(d>0 && poop-10>=0){v-=10; d--; poop=v%100;}
                while(n>0 && poop-5>=0){v-=5; n--; poop=v%100;}
                v=0;
                if(x.equals("q")){q++; v+=25;alarm1 = r + 2.0; alarm2=0;}
                if(x.equals("d")){d++; v+=10;alarm1 = r + 2.0; alarm2=0;}
                if(x.equals("n")){n++; v+=5;alarm1 = r + 2.0; alarm2=0;}
                if(x.equals("c")){
                    ccc=true; alarm1 = r; alarm2++;
                }

                //alarm1 = r + 2.0; alarm2=0;
                list1.remove(0); list2.remove(0); list3.remove(0);
                rr=r;xx=x;

            }

        }

        //final alarm clock(s)

        while(alarm1<999999999){

            r=alarm1; c=alarm2;
            e = r-rr;

            //lambda
            System.out.print("\n "+ r + "," + c + "    Λ i: ");

            if(cc){

                System.out.print("c");
                //delta
                cc=false;
                alarm1 = r + 2.0 - e; alarm2=0;


            }else{

            int poop = v / 100;
            for(int i=0;i<poop;i++){System.out.print("coffee ");}
            poop = v % 100;
            int qq=q; while(qq>0 && poop-25>=0){poop-=25; qq--; System.out.print("q");}
            int dd=d; while(dd>0 && poop-10>=0){poop-=10; dd--; System.out.print("d");}
            int nn=n; while(nn>0 && poop-5>=0){poop-=5; nn--; System.out.print("n");}
            if(poop>0) System.out.print(" and that's all i got ");

            //delta
            poop = v % 100;
            while(q>0 && poop-25>=0){v-=25; q--; poop=v%100;}
            while(d>0 && poop-10>=0){v-=10; d--; poop=v%100;}
            while(n>0 && poop-5>=0){v-=5; n--; poop=v%100;}
            v=0;
            alarm1 = 999999999; alarm2 = 0;

            }rr=r;
        }
















    }


}





/*






//call delta external
                if(x.equals("q")){q++; v+=25;alarm1 = r + 2.0; alarm2=0;rr=r;}
                        if(x.equals("d")){d++; v+=10;alarm1 = r + 2.0; alarm2=0;rr=r;}
                        if(x.equals("n")){n++; v+=5;alarm1 = r + 2.0; alarm2=0;rr=r;}
                        if(x.equals("c")){cc=true; alarm1 = r; alarm2++;}
//alarm1 = r + 2.0; alarm2=0;

                        if(cc){

                        System.out.print("c");
                        //delta
                        cc=false;
                        alarm1 = r + 2.0 - e; alarm2=0;


                        }else{



 */