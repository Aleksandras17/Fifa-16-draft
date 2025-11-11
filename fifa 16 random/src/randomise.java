import edu.duke.*;
import org.apache.commons.csv.*;
import java.util.*;
public class randomise {
    private int[] weights;
    private Random myRandom;
    private String pos;
    private int[] up;
    private int[] low;
    private int[] mid;
    private ArrayList<Player> pList;

    public randomise(String position){
        pos = position;
        myRandom = new Random();
        pList= new ArrayList<Player>();

        setWeights();

        setInterval(pos);

        makeList();

    }

    private void setWeights(){
        int[] weight = {43, 50, 7};
        weights = new int[100];
        for(int i=0; i<100; i++){
            if(i<weight[0])
                weights[i]=0;
            else if(i<weight[0]+weight[1])
                weights[i]=1;
            else
                weights[i]=2;
        }
    }

    private void setInterval(String pos){
        if(pos.equals("RW"))
            mid = new int[]{78, 80};
        else if (pos.equals("ST") || pos.equals("CB") || pos.equals("GK") || pos.equals("LW"))
            mid = new int[]{78, 82};
        else if (pos.equals("CAM") || pos.equals("CM") || pos.equals("MID"))
            mid = new int[]{78, 83};
        else if (pos.equals("CDM") || pos.equals("LB") || pos.equals("ATT"))
            mid = new int[]{78, 81};
        else if (pos.equals("RB"))
            mid = new int[]{77,80};
        else if(pos.equals("DEF"))
            mid = new int[]{77, 81};
        else
            mid = new int[]{78, 81};


        up = new int[]{ mid[0]-1 , mid[1] , 99};
        low = new int[]{ 75, mid[0] , mid[1]+1};
    }


    public class Player {
        private String name;
        private int rating;
        private String club;
        private String foot;
        private String position;
        private int weakFoot;
        private int skillMoves;
        private int pace;
        private int shooting;
        private int passing;
        private int dribbling;
        private int defending;
        private int physical;

        public Player(CSVRecord rec) {
            this.name = rec.get(0).trim();
            this.rating = Integer.parseInt(rec.get(1).trim());
            this.club = rec.get(2).trim();
            this.foot = rec.get(3).trim();
            this.position = rec.get(4).trim();
            this.weakFoot = Integer.parseInt(rec.get(5).trim());
            this.skillMoves = Integer.parseInt(rec.get(6).trim());
            this.pace = Integer.parseInt(rec.get(7).trim());
            this.shooting = Integer.parseInt(rec.get(8).trim());
            this.passing = Integer.parseInt(rec.get(9).trim());
            this.dribbling = Integer.parseInt(rec.get(10).trim());
            this.defending = Integer.parseInt(rec.get(11).trim());
            this.physical = Integer.parseInt(rec.get(12).trim());
        }

        public String getPosition() { return position; }
        public int getRating() { return rating; }

        @Override
        public String toString() {
            return name + ", " + rating+ ", " + club + ", " + position + ", " + weakFoot +", " + skillMoves + ", " + pace + ", " + shooting + ", " + passing + ", " + dribbling + ", " + defending + ", " + physical ;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Player other = (Player) obj;

            return name.equals(other.name) &&
                    position.equals(other.position) &&
                    rating == other.rating;
        }
    }

    public void GetPlayers(){

        Player p;


        ArrayList<Player> draft = new ArrayList<>();
        if (pos.equals("CAP") ){
            for(int i=0; i<5; i++){
                p=singleBound();
                draft.add(p);
                pList.remove(p);
            }
        }
        else if(pos.equals("RES")){
            for(int i=0; i<5; i++){
                p=singleBound();
                draft.add(p);
                pList.remove(p);
            }
        }
        else{
            ArrayList<Player> lList = FilterByRating(low[0], up[0]);
            ArrayList<Player> mList = FilterByRating(low[1], up[1]);
            ArrayList<Player> hList = FilterByRating(low[2], up[2]);

            for(int i=0; i<5; i++){
                int level = weights[myRandom.nextInt(100)];
                if(level ==0)
                    draft.add(multiBound(lList));
                else if(level ==1)
                    draft.add(multiBound(mList));
                else
                    draft.add(multiBound(hList));
            }
        }
        for(Player p1: draft)
            System.out.println(p1.toString());
    }

    private void makeList(){

        FileResource fr1 = new FileResource("src/Resource/playerFile.csv");
        CSVParser players = fr1.getCSVParser();
        for (CSVRecord rec: players){
            Player p = new Player(rec);
            if(matchCriteria(p))
                pList.add(p);
        }

    }



    private ArrayList<Player> FilterByRating(int low, int up){
        ArrayList<Player> fList = new ArrayList<>();
        for (Player p: pList){
            if(p.getRating()>=low && p.getRating()<=up)
                fList.add(p);
        }
        return fList;
    }

    private boolean matchCriteria(Player p){
        int rating = p.getRating();
        String position = p.getPosition();

        switch (pos) {
            case "DEF":
                return position.equals("CB") || position.equals("RB") || position.equals("LB");
            case "ATT":
                return position.equals("ST") || position.equals("RW") || position.equals("LW");
            case "MID":
                return position.equals("CM") || position.equals("CAM") || position.equals("CDM");
            case "CAP":
                return rating>=82;
            case "RES":
                return rating >=78;



            default:
                return position.equals(pos);
        }
    }


    private Player singleBound(){
        int pNum = myRandom.nextInt(pList.size());
        return pList.get(pNum);
    }

    private Player multiBound(ArrayList<Player> list){
        return list.remove(myRandom.nextInt(list.size()));
    }

    static public void main(String[] args){
        randomise ran = new randomise("RES");
        //{"CAP", "POS", "RES", "MID", "ATT", "DEF"
        ran.GetPlayers();
    }
}
