import java.util.PriorityQueue;

public class Main {

    public static void main(String[] args){

        Press press = new Press();
        Drill drill = new Drill();
        PriorityQueue<Event> pq = new PriorityQueue<>();
        pq.add(new Event(new Time(3,0),3,-1,true,false,false,false,false,false));
        pq.add(new Event(new Time(2,0),2,-1,true,false,false,false,false,false));
        pq.add(new Event(new Time(Double.POSITIVE_INFINITY,0),-1,-1,false,true,false,false,false,false));
        pq.add(new Event(new Time(Double.POSITIVE_INFINITY,0),-1,-1,false,false,false,false,true,false));

        Event prevEvent = null;

        while(!pq.isEmpty())
        {
            boolean exists;
            Event event = pq.poll();

            if(event.t.r<Double.POSITIVE_INFINITY){
            System.out.println(event.t.r);

            //if external and internal, confluent
            if(event.pressExt && event.pressInt){event.pressExt=false; event.pressInt=false; event.pressCon=true;}
            if(event.drillExt && event.drillInt){event.drillExt=false; event.drillInt=false; event.drillCon=true;}

            //time
            Time t = event.t;
            //elapsed time
            double e = t.r; if(prevEvent!=null){e -= prevEvent.t.r;}
            //input
            int x = event.x; int xx = event.xx;

            //call all applicable in following order

            //lambda press
            if(event.pressInt||event.pressCon){
                xx = press.lambda();
            }
            //lambda drill
            if(event.drillInt||event.drillCon){
                drill.lambda();
            }

            //coupling
            if(event.pressInt||event.pressCon){
                if(event.drillInt){event.drillInt=false;event.drillCon=true;}else{event.drillExt=true;}
                //this is short for
                //event.drillExt=true; if(event.drillInt){event.drillInt=false; event.drillExt=false; event.drillCon=true;}
            }

            //delta internal press
            if(event.pressInt){
                press.deltaInternal();
                Time alarm = event.t.timeAdvance(new Time(press.s,0), press.p);

                //if already exists
                exists=false;
                for(Event eee : pq){if(eee.t.r==alarm.r && eee.t.c==alarm.c){eee.pressInt=true;exists=true;break;}}
                if(!exists){pq.add(new Event(alarm,-1,-1,false,true,false,false,false,false));}

            }
            //delta internal drill
            if(event.drillInt){
                drill.deltaInternal();
                Time alarm = event.t.timeAdvance(new Time(drill.s,0), drill.p);

                //if already exists
                exists=false;
                for (Event eee:pq){if(eee.t.r==alarm.r && eee.t.c==alarm.c){eee.drillInt=true;exists=true;break;}}
                if(!exists){pq.add(new Event(alarm,-1,-1,false,false,false,false,true,false));}

            }
            //delta external press
            if(event.pressExt){
                press.deltaExternal(e,x);
                Time alarm = event.t.timeAdvance(new Time(press.s,0), press.p);

                //if already exists
                exists=false;
                for (Event eee:pq){if(eee.t.r==alarm.r && eee.t.c==alarm.c){eee.pressInt=true;exists=true;break;}}
                if(!exists){pq.add(new Event(alarm,-1,-1,false,true,false,false,false,false));}

            }
            //delta external drill
            if(event.drillExt){
                drill.deltaExternal(e,xx);
                Time alarm = event.t.timeAdvance(new Time(drill.s,0), drill.p);

                //if already exists
                exists=false;
                for (Event eee:pq){if(eee.t.r==alarm.r && eee.t.c==alarm.c){eee.drillInt=true;exists=true;break;}}
                if(!exists){pq.add(new Event(alarm,-1,-1,false,false,false,false,true,false));}

            }
            //delta confluent press
            if(event.pressCon){
                press.deltaConfluent(e,x);
                Time alarm = event.t.timeAdvance(new Time(press.s,0), press.p);

                //if already exists
                exists=false;
                for (Event eee:pq){if(eee.t.r==alarm.r && eee.t.c==alarm.c){eee.pressInt=true;exists=true;break;}}
                if(!exists){pq.add(new Event(alarm,-1,-1,false,true,false,false,false,false));}

            }
            //delta confluent drill
            if(event.drillCon){
                drill.deltaConfluent(e,xx);
                Time alarm = event.t.timeAdvance(new Time(drill.s,0), drill.p);

                //if already exists
                exists=false;
                for (Event eee:pq){if(eee.t.r==alarm.r && eee.t.c==alarm.c){eee.drillInt=true;exists=true;break;}}
                if(!exists){pq.add(new Event(alarm,-1,-1,false,false,false,false,true,false));}

            }

            }else{break;}
            prevEvent = event;
        }
    }
}

public class Time {

    double r; int c;

    Time(double rr, int cc){r=rr;c=cc;}

    public Time timeAdvance(Time interval, int p){

        if(p>0){
            if(interval.r == 0){
                return new Time(this.r, this.c + interval.c);
            }else{return new Time(this.r + interval.r, 0);}
        }else return new Time(Double.POSITIVE_INFINITY,0);

    }


}

public class Press {

    int p = 0; //number of parts in the system
    double s = Double.POSITIVE_INFINITY; //time remaining to press part now being pressed
    int t=1; //time it takes to press one part

    Press(){}

    int lambda(){return 1;}

    void deltaInternal(){p--; s=t;}

    void deltaExternal(double e, int q){
        if(p>0){
            s-=e;
        }else{s=t;}
        p+=q;
    }

    void deltaConfluent(double e, int q){p += q-1; s=t;}

}

public class Event implements Comparable<Event>{

    Time t;
    int x; int xx;
    boolean pressExt;
    boolean pressInt;
    boolean pressCon;
    boolean drillExt;
    boolean drillInt;
    boolean drillCon;


    Event(Time t, int x, int xx, boolean pe, boolean pi, boolean pc, boolean de, boolean di, boolean dc){
        this.t=t; this.x=x; this.xx=xx;
        pressExt=pe;
        pressInt=pi;
        pressCon=pc;
        drillExt=de;
        drillInt=di;
        drillCon=dc;
    }


    @Override
    public int compareTo(Event o) {
        // Compare based on r values
        if (t.r > o.t.r) {
            return 1;
        } else if (t.r < o.t.r) {
            return -1;
        } else {
            // If r values are equal, compare based on c values
            if (t.c > o.t.c) {
                return 1;
            } else if (t.c < o.t.c) {
                return -1;
            } else {
                // If both r and c values are equal, return 0 (objects are equal)
                return 0;
            }
        }
    }

}

public class Drill {

    int p = 0; //number of parts in the system
    double s = Double.POSITIVE_INFINITY; //time remaining to drill part now being drilled
    int t=2; //time it takes to drill one part

    Drill(){}

    int lambda(){System.out.println("1");return 1;}

    void deltaInternal(){p--; s=t;}

    void deltaExternal(double e, int q){
        if(p>0){
            s-=e;
        }else{s=t;}
        p+=q;
    }

    void deltaConfluent(double e, int q){p += q-1; s=t;}

}

