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

