import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author notda
 */
public class MainGameStageController implements Initializable {

    @FXML
    private Parent root;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Button flodBtn, checkBtn, callBtn, raiseBtn, btn500, btn100, btn50, btn20, allInBtn, resetBtn, confirmBtn, mainMenuBtn, restartBtn,continueBtn;
    @FXML
    private Label comBalance, potBalance, playerName, playerBalance, playerOnHand, comOnHand, comPlay, playerPlay, playerRiseBalance,winLebal;
    @FXML
    private Circle comButton, playerBtn;
    @FXML
    private ImageView player01, player02, flop01, flop02, flop03, turn, river, com01, com02;

    Random rand = new Random();
    private int[] suitPot = new int[5];
    private int[] numPot = new int[5];
    private int[] suitPlay = new int[2];
    private int[] numPlay = new int[2];
    private int[] suitCom = new int[2];
    private int[] numCom = new int[2];
    private int[][] recheck = new int[4][13];
    private int turnPlayed = 0;
    private int TURNPlay = 0;
    private int playerLeftBalance = 0;
    private int comLeftBalance = 0;
    private int potLeftBalance = 0;
    private String playerHasPlay = "";
    private String comHasPlay = "";
    private int comRaise = 0;
    private int playerRaise = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        turnPlayed = 0;
        TURNPlay = 0;
        playerName.setText("Player");
        playerShowBalance(1500,0);
        comShowBalance(1500, 0);
        potShowBalance(0, 0);
        playerHasPlay = "";
        comHasPlay = "";
        try {
            firstTurn(TURNPlay);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainGameStageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /* FIX!! */
    @FXML
    private void mainMenuBtnAction(ActionEvent event)throws IOException{
        root = FXMLLoader.load(getClass().getResource("Mainmenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    private void firstTurn (int TURNPlay) throws InterruptedException{
        System.out.println("first");
        com01.setOpacity(1);
        com02.setOpacity(1);
        player01.setOpacity(1);
        player02.setOpacity(1);
        turnPlayed = 0;
        randCard();
        continueBtn.setOpacity(0);
        continueBtn.setDisable(true);
        winLebal.setText("");
        loadImageTurn(0);
        showHandRank(0, "player");
        comOnHand.setText("");
        playerRiseBalance.setText("0");
        setButton(TURNPlay);
        if(TURNPlay%2 == 0) //Player is 1st player
        {
            playerHasPlay = "raise";
            playerPlay.setText(playerHasPlay);
            if(playerLeftBalance >= 50)
            {
                playerShowBalance(0, 50);
                potShowBalance(50, 0);
                playerRaise = 50;
            } else {
                playerShowBalance(0, playerLeftBalance);
                potShowBalance(playerLeftBalance, 0);
                playerRaise = playerLeftBalance;
            }
            comTurn(turnPlayed, playerHasPlay);
        } else //Com is 1st player
        {
            comHasPlay = "raise";
            comPlay.setText(comHasPlay);
            if(comLeftBalance >= 50)
            {
                comShowBalance(0, 50);
                potShowBalance(50, 0);
                comRaise = 50;
            } else {
                comShowBalance(0, comLeftBalance);
                potShowBalance(comLeftBalance, 0);
                comRaise = comLeftBalance;
            }
            playerTurn(comHasPlay);
        }
    }
        
    private void normalTurn (int turnPlayed, int TURNPlay) throws InterruptedException{
        System.out.println("normal");
        this.turnPlayed++;
        turnPlayed = this.turnPlayed;
        System.out.println(this.turnPlayed);
        playerHasPlay = "";
        comHasPlay = "";
        loadImageTurn(turnPlayed);
        showHandRank(turnPlayed, "player");
        System.out.println("?");
        if(turnPlayed == 4 )
        { 
            System.out.println("goto last");
            lastTurn();
        } else if (TURNPlay%2 == 0) //Player is 1st player
        {
            System.out.println("1");
            playerTurn(comHasPlay);
        } else { System.out.println("2"); comTurn(turnPlayed, playerHasPlay);}
    }
    
    private void lastTurn () throws InterruptedException{
        System.out.println("last");
        callBtn.setDisable(true);
        flodBtn.setDisable(true);
        checkBtn.setDisable(true);
        raiseBtn.setDisable(true);
        if (comHasPlay == "flod")
        {
        winLebal.setText("You win");
        continueBtn.setOpacity(1);
        continueBtn.setDisable(false); 
        playerShowBalance(potLeftBalance, 0);
        potShowBalance(0, potLeftBalance);
        } else if (playerHasPlay == "flod")
        {
        winLebal.setText("Computer win");
        continueBtn.setOpacity(1);
        continueBtn.setDisable(false);
        comShowBalance(potLeftBalance,0);
        potShowBalance(0, potLeftBalance);
        }else{
        loadImageTurn(4);
        showHandRank(4, "com");
        showHandRank(4, "player");
            if(compareHandRank() == 0)// 0 = playerWin, 1 = comWin, 2 = Draw
            {
                playerShowBalance(potLeftBalance, 0);
                potShowBalance(0, potLeftBalance);
            } else if (compareHandRank() == 1){
                comShowBalance(potLeftBalance,0);
                potShowBalance(0, potLeftBalance);
            } else {
                playerShowBalance(potLeftBalance/2, 0);
                comShowBalance(potLeftBalance/2,0);
                potShowBalance(0, potLeftBalance);
            }
        winLebal.setText(showcompareHandRank());
        continueBtn.setOpacity(1);
        continueBtn.setDisable(false);
        }
        
        if(this.comLeftBalance == 0 || this.playerLeftBalance == 0)
        {endGame();}
    }
    
    private void setButton (int TURNPlay)
    {
        //System.out.println("SetButton");
        if(TURNPlay % 2 == 0) //Player is 1st player
        {
            playerBtn.setVisible(true);
            comButton.setVisible(false);
        } else //Com is 1st player
        {
            playerBtn.setVisible(false);
            comButton.setVisible(true);
        }
    }
    
    private void setBtn (String comHasPlay)
    {
        if(comLeftBalance == 0 || playerLeftBalance == 0)
        {
            flodBtn.setDisable(true);
            checkBtn.setDisable(false);
            callBtn.setDisable(true);
            raiseBtn.setDisable(true);
        } else if(comHasPlay == "check")
        {
            flodBtn.setDisable(false);
            checkBtn.setDisable(false);
            callBtn.setDisable(true);
            raiseBtn.setDisable(false);
        } else if(comHasPlay == "call")
        {
            flodBtn.setDisable(false);
            checkBtn.setDisable(false);
            callBtn.setDisable(true);
            raiseBtn.setDisable(false);
        } else if(comHasPlay == "raise")
        {
            flodBtn.setDisable(false);
            checkBtn.setDisable(true);
            callBtn.setDisable(false);
            raiseBtn.setDisable(false);
        } else{
            flodBtn.setDisable(false);
            checkBtn.setDisable(false);
            callBtn.setDisable(true);
            raiseBtn.setDisable(false);
        }
    }
   
    
    private String showcompareHandRank (){
        if(compareHandRank() == 1)
        {
            return "Computer win";
        } else if(compareHandRank() == 0)
        {
            return "You win";
        } else {return "Draw";}
    }
    
    private int compareHandRank (){ // 0 = playerWin, 1 = comWin, 2 = Draw
        if(checkHandRank(4, "com") > checkHandRank(4, "player"))
        {
            return 1;
        } else if(checkHandRank(4, "player") > checkHandRank(4, "com"))
        {
            return 0;
        } else {return 2;}
    }
    
    
    private void comTurn (int turnPlayed , String playerHasplay) throws InterruptedException
    {
        comPlay.setText("Computer's Turn");
        flodBtn.setDisable(true);
        callBtn.setDisable(true);
        checkBtn.setDisable(true);
        raiseBtn.setDisable(true);
        if(turnPlayed == 0 && playerHasplay == "raise")
        {
            comPlayCall();
        } else if (playerLeftBalance == 0 || comLeftBalance == 0)
        {
            if(playerHasplay == "raise")
            {
                if(rand.nextInt(2) == 0)
                    {
                        comPlayCall();
                    } else {comPlayFlod();}
            } else comPlayCheck(turnPlayed, TURNPlay);
        }else {
            if(checkHandRank(turnPlayed, "com") == 1)
            {
                if(playerHasplay == "raise")
                {
                    if(rand.nextInt(4) == 0)
                    {
                        comPlayCall();
                    } else {comPlayFlod();}
                }else if((rand.nextInt(120)+1)%60 == 0)
                {
                    comPlayRaise(50);
                } else {comPlayCheck(turnPlayed, TURNPlay);}
            } else if(checkHandRank(turnPlayed, "com") <= 5)
            {
                if(playerHasplay == "raise")
                {
                    if(rand.nextInt(2) == 0)
                    {
                        comPlayCall();
                    } else {comPlayFlod();}
                } else if((rand.nextInt(120)+1)%20 == 0)
                {
                    comPlayRaise(50);
                } else if((rand.nextInt(120)+1)%20 == 0)
                {
                    comPlayRaise(100);
                } else if((rand.nextInt(120)+1)%40 == 0)
                {
                    comPlayRaise(200);
                } else if((rand.nextInt(120)+1)%60 == 0)
                {
                    comPlayRaise(500);
                } else {comPlayCheck(turnPlayed, TURNPlay);}
            } else {
                if(playerHasplay == "raise")
                {
                    comPlayCall();
                } else if((rand.nextInt(120)+1)%40 == 0)
                {
                    comPlayRaise(100);
                } else if((rand.nextInt(120)+1)%20 == 0)
                {
                    comPlayRaise(200);
                } else if((rand.nextInt(120)+1)%10 == 0)
                {
                    comPlayRaise(500);
                } else {comPlayCheck(turnPlayed, TURNPlay);}
            }
        }
    }
    
    private void comPlayCheck (int turnPlayed,int TURNPlay) throws InterruptedException{
        showComPlay("check");
        if(playerHasPlay == "check")
        {
            
            normalTurn(turnPlayed, TURNPlay);
        } else {
            playerTurn(comHasPlay);
        }
    }
    
    private void comPlayCall() {
        showComPlay("call");
        System.out.println(playerRaise);
        if(comLeftBalance >= playerRaise)
        {
        comShowBalance(0, playerRaise);
        potShowBalance(playerRaise, 0);
        } else {
        comShowBalance(0, comLeftBalance);
        potShowBalance(comLeftBalance, 0);   
        }
        playerTurn(comHasPlay);
    }
    
    private void comPlayFlod() throws InterruptedException {
        showComPlay("flod");
        com01.setOpacity(0.5);
        com02.setOpacity(0.5);
        lastTurn();
    }
    
    private void comPlayRaise(int raise){
        showComPlay("raise");
        comRaise = 0;
        if(comLeftBalance >= raise)
        {
        comShowBalance(0, raise);
        potShowBalance(raise, 0);
        comRaise = raise;
        } else {
        comShowBalance(0, comLeftBalance);
        potShowBalance(comLeftBalance, 0);
        comRaise = comLeftBalance;
        }
        playerTurn(comHasPlay);
    }
    
    private void showComPlay(String comPlay)
    {
        comHasPlay = comPlay;
        this.comPlay.setText(comPlay);
    }
    
    private void playerTurn (String comHasPlay)
    {
        playerPlay.setText("Player" + "'s Turn");
        setBtn(comHasPlay);
    }
    
    private void endGame ()
    {
        continueBtn.setDisable(true);
        continueBtn.setOpacity(0);
        if(this.playerLeftBalance == 0)
        {
        winLebal.setText("Computer is WINNER");
        } else {winLebal.setText("Player is WINNER");}
    }
    
    @FXML
    private void flodBtnAction(ActionEvent event) throws InterruptedException {
        playerHasPlay = "flod";
        playerPlay.setText("flod");
        player01.setOpacity(0.5);
        player02.setOpacity(0.5);
        lastTurn();
    }

    @FXML
    private void checkBtnAction(ActionEvent event) throws InterruptedException {
        System.out.println("pushCheck");
        playerHasPlay = "check";
        playerPlay.setText("check");
        if(comHasPlay == "check")
        {
            normalTurn(turnPlayed, TURNPlay);
        } else {
            comTurn(turnPlayed, playerHasPlay);
        }
    }

    @FXML
    private void callBtnAction(ActionEvent event) throws InterruptedException {
        playerHasPlay = "call";
        playerPlay.setText("call");
        if(playerLeftBalance >= comRaise)
        {
        playerShowBalance(0, comRaise);
        potShowBalance(comRaise, 0);
        } else {
        playerShowBalance(0, playerLeftBalance);
        potShowBalance(playerLeftBalance, 0); 
        }
        comTurn(turnPlayed, playerHasPlay);
    }

    @FXML
    private void raiseBtnAction(ActionEvent event) {
        playerHasPlay = "raise";
        playerPlay.setText("raise");
        flodBtn.setDisable(true);
        callBtn.setDisable(true);
        checkBtn.setDisable(true);
        setPlayerRise(0, playerRaise);
        setRaiseBtn(playerLeftBalance);
    }

    private void setRaiseBtn(int playerLeftBalance){
        if(playerLeftBalance >= 500)
        {
            btn500.setDisable(false);
        }
        if(playerLeftBalance >= 100)
        {
            btn100.setDisable(false);
        }
        if(playerLeftBalance >= 50)
        {
            btn50.setDisable(false);
        }
        if(playerLeftBalance >= 20)
        {
            btn20.setDisable(false);
        }
        allInBtn.setDisable(false);
        confirmBtn.setDisable(false);
        resetBtn.setDisable(false);
        playerRiseBalance.setDisable(false);
    }
   
    
    @FXML
    private void btn500Action(ActionEvent event) {
        setPlayerRise(500,0);
    }

    @FXML
    private void btn100Action(ActionEvent event) {
        setPlayerRise(100,0);
    }

    @FXML
    private void btn50Action(ActionEvent event) {
        setPlayerRise(50,0);
    }

    @FXML
    private void btn20Action(ActionEvent event) {
        setPlayerRise(20,0);
    }

    @FXML
    private void allInBtnAction(ActionEvent event) {
        setPlayerRise(playerLeftBalance, 0);
    }

    @FXML
    private void resetBtnAction(ActionEvent event) {
        setPlayerRise(0, playerRaise);
    }

    @FXML
    private void confirmBtnAction(ActionEvent event) throws InterruptedException {
        playerShowBalance(0, playerRaise);
        potShowBalance(playerRaise, 0);
        //System.out.println(playerRaise);
        btn500.setDisable(true);
        btn100.setDisable(true);
        btn50.setDisable(true);
        btn20.setDisable(true);
        resetBtn.setDisable(true);
        allInBtn.setDisable(true);
        playerRiseBalance.setDisable(true);
        confirmBtn.setDisable(true);
        comTurn(turnPlayed, playerHasPlay);
    }

    @FXML
    private void restartBtnAction(ActionEvent event) throws InterruptedException {
        playerShowBalance(0,playerLeftBalance);
        comShowBalance(0, comLeftBalance);
        potShowBalance(0, potLeftBalance);
        turnPlayed = 0;
        TURNPlay = 0;
        playerName.setText("Player");
        playerShowBalance(1500,0);
        comShowBalance(1500, 0);
        potShowBalance(0, 0);
        playerHasPlay = "";
        comHasPlay = "";
        firstTurn(TURNPlay);
    }
    
    @FXML
    private void continueBtnAction(ActionEvent event) throws InterruptedException {
        TURNPlay++;
        firstTurn(TURNPlay);
    }
    
    private void setPlayerRise(int plus, int minus)
    {
        if(playerRaise+plus <= playerLeftBalance)
        {
        playerRaise = playerRaise + plus - minus;
        playerRiseBalance.setText(String.valueOf(playerRaise));
        } else {
            playerRaise = playerLeftBalance;
            playerRiseBalance.setText(String.valueOf(playerRaise));
        }
    }
    
    private void playerShowBalance(int plus, int minus)
    {
        //System.out.println("playerShowBalance");
        playerLeftBalance += plus;
        playerLeftBalance -= minus;
        playerBalance.setText(String.valueOf(playerLeftBalance));
    }
    
    private void comShowBalance(int plus, int minus)
    {
        //System.out.println("comShowBalance");
        comLeftBalance += plus;
        comLeftBalance -= minus;
        comBalance.setText(String.valueOf(comLeftBalance));
    }
    
    private void potShowBalance(int plus, int minus)
    {
        //System.out.println("potShowBalance");
        potLeftBalance += plus;
        potLeftBalance -= minus;
        potBalance.setText(String.valueOf(potLeftBalance));
    }

    private void randCard() { //random Card 
        //System.out.println("randCard");
        for(int i = 0; i < 4 ; i++)
        {
            for(int j = 0 ; j < 13 ; j++)
            {
                recheck[i][j] = 0 ;
            }
        }
        for (int i = 0; i < 9; i++) {
            int suit;
            int num;
            do {
                suit = rand.nextInt(4); //Club = 0, Spade = 1, Heart = 2,Diamond = 3
                num = rand.nextInt(13) + 2; // 2 = 2, 3 = 3 ... J = 11, Q = 12, K = 13, A = 14 
                recheck[suit][num - 2]++; 
            } while (recheck[suit][num - 2] > 1); 
            if (i < 5) { //random card in POT
                suitPot[i] = suit;
                numPot[i] = num;
            } else if (i < 7) { //random card player
                suitPlay[i - 5] = suit;
                numPlay[i - 5] = num;
            } else { //random card Com
                suitCom[i - 7] = suit;
                numCom[i - 7] = num;
            }

        }
    }

    private void loadImage(ImageView imageView, int suit, int num) { //get Image from pic *00 is back Card
        //System.out.println("loadImage");
        Image image = new Image(getClass().getResourceAsStream("pic/" + suit + num + ".png"));
        imageView.setImage(image);
    }

    private void loadImageTurn(int turnPlayed)
    {
        //System.out.println("loadImageTurn");
        if(turnPlayed >= 0)
        {
        loadImage(player01, suitPlay[0], numPlay[0]);
        loadImage(player02, suitPlay[1], numPlay[1]);
        loadImage(flop01, 0, 0);
        loadImage(flop02, 0, 0);
        loadImage(flop03, 0, 0);
        loadImage(turn, 0, 0);
        loadImage(river, 0, 0);
        loadImage(com01, 0, 0);
        loadImage(com02, 0, 0);
        }
        if(turnPlayed >= 1)
        {
        loadImage(flop01, suitPot[0], numPot[0]);
        loadImage(flop02, suitPot[1], numPot[1]);
        loadImage(flop03, suitPot[2], numPot[2]);
        }
        if(turnPlayed >= 2)
        {
        loadImage(turn, suitPot[3], numPot[3]);
        }
        if(turnPlayed >= 3)
        {
        loadImage(river, suitPot[4], numPot[4]);
        }
        if(turnPlayed >= 4)
        {
        loadImage(com01, suitCom[0], numCom[0]);
        loadImage(com02, suitCom[1], numCom[1]);
        }

    }
    
    private ArrayList collectCardPlayer(int turn, String type){ 
        System.out.println("collectCardPlayer");
        ArrayList<Integer> collectCard = new ArrayList<>(); //CollectCard Pot-Player / turn
        if("num".equals(type))
        {
        for (int i = 0; i < 2; i++) {
            //System.out.println("play");
            collectCard.add(numPlay[i]);
            }
        if (turn >= 3) {
            for (int i = 0; i < 5; i++) {
                collectCard.add(numPot[i]);
                //System.out.println("pot");
            }
        }else if (turn >= 1) {
            for (int i = 0; i < 2 + turn; i++) {
                collectCard.add(numPot[i]);
                //System.out.println("pot");
                }
            }
        } else if (type == "suit")
        {
           for (int i = 0; i < 2; i++) {
               //System.out.println("Splay");
            collectCard.add(suitPlay[i]);
            }
        if (turn >= 3) {
            for (int i = 0; i < 5; i++) {
                collectCard.add(suitPot[i]);
                //System.out.println("pot");
            }
        }else if (turn >= 1) {
            for (int i = 0; i < 2 + turn; i++) {
                //System.out.println("Spot");
                collectCard.add(suitPot[i]);
                }
            } 
        }
        
        return collectCard;
    }
    
    private ArrayList collectCardCom(int turn, String type){
        System.out.println("collectCardCom");
        ArrayList<Integer> collectCard = new ArrayList<>(); //CollectCard Pot-Com / turn
        if("num".equals(type))
        {
        for (int i = 0; i < 2; i++) {
            collectCard.add(numCom[i]);
            }
        if (turn >= 3) {
            for (int i = 0; i < 5; i++) {
                collectCard.add(numPot[i]);
                //System.out.println("pot");
            }
        }else if (turn >= 1) {
            for (int i = 0; i < 2 + turn; i++) {
                collectCard.add(numPot[i]);
                }
            }
        } else if (type == "suit")
        {
           for (int i = 0; i < 2; i++) {
            collectCard.add(suitCom[i]);
            }
        if (turn >= 3) {
            for (int i = 0; i < 5; i++) {
                collectCard.add(suitPot[i]);
                //System.out.println("pot");
            }
        }else if (turn == 1 || turn == 2) {
            for (int i = 0; i < 2 + turn; i++) {
                collectCard.add(suitPot[i]);
                }
            } 
        }
        
        
        return collectCard;
    }    
    
    
    private int checkStraight(int turn ,String who) {
        System.out.println("checkStraight");
        int isStraight = 0;
        int check = 0;
        ArrayList<Integer> collectCard = new ArrayList<>();
        
        if(who == "player")
        { collectCard = collectCardPlayer(turn, "num"); //CollectCard Pot-Player / turn 
        } else collectCard = collectCardCom(turn, "num"); //CollectCard Pot-Com / turn 

        if (turn > 0) { //CheckStraight
            Collections.sort(collectCard);
            for (int i = 0; i < collectCard.size() - 1; i++) {
                while(Objects.equals(collectCard.get(i), collectCard.get(i + 1)) && i != collectCard.size()-2)
                { i++; }
                if (Objects.equals(collectCard.get(i)+1, collectCard.get(i + 1)) && i != collectCard.size()-2) 
                { 
                    check++;
                } else {
                    check = 0;
                }
                
                if(check == 4 && collectCard.get(collectCard.size()-1) == 14)
                {
                    isStraight = 2;
                } else if(check == 4)
                {
                    isStraight = 1;
                }
            }
        }

        return isStraight;

    }
    
    private boolean checkFlush(int turn , String who){
        //System.out.println("checkFlush");
        boolean isFlush = false;
        int check = 0;
        ArrayList<Integer> collectCard = new ArrayList<>();
        
        if(who == "player")
        { collectCard = collectCardPlayer(turn, "suit"); //CollectCard Pot-Player / turn 
        } else collectCard = collectCardCom(turn, "suit"); //CollectCard Pot-Com / turn 

        if (turn > 0) { //CheckFlush
            Collections.sort(collectCard);
            for (int i = 0; i < collectCard.size() - 1; i++) {
                if (Objects.equals(collectCard.get(i), collectCard.get(i + 1))) {
                    check++;
                } else {
                    check = 0;
                }
                
                if(check == 4)
                {
                    isFlush = true;
                }
            }
        }

        return isFlush;
    }
    
    private boolean checkFourOfKind(int turn , String who)
    {
        //System.out.println("checkFourOfKind");
        boolean isFOK = false;
        int check = 0;
        ArrayList<Integer> collectCard = new ArrayList<>();
        
        if(who == "player")
        { collectCard = collectCardPlayer(turn, "num"); //CollectCard Pot-Player / turn 
        } else collectCard = collectCardCom(turn, "num"); //CollectCard Pot-Com / turn 

        if (turn > 0) { //CheckFourOfKind
            Collections.sort(collectCard);
            for (int i = 0; i < collectCard.size() - 1; i++) {
                if (Objects.equals(collectCard.get(i), collectCard.get(i + 1))) {
                    check++;
                } else {
                    check = 0;
                }
                
                if(check == 3)
                {
                    isFOK = true;
                }
            }
        }

        return isFOK;
    }
    
    private int checkThreeOfKind(int turn , String who)
    {
        //System.out.println("checkThreeOfKind");
        int numOfTOK = 0;
        int check = 0;
        ArrayList<Integer> collectCard = new ArrayList<>();
        
        if(who == "player")
        { collectCard = collectCardPlayer(turn, "num"); //CollectCard Pot-Player / turn 
        } else collectCard = collectCardCom(turn, "num"); //CollectCard Pot-Com / turn 

        if (turn > 0) { //CheckThreeOfKind
            Collections.sort(collectCard);
            for (int i = 0; i < collectCard.size() - 1; i++) {
                if (Objects.equals(collectCard.get(i), collectCard.get(i + 1))) {
                    check++;
                } else {
                    check = 0;
                }
                
                if(check == 2)
                {
                    numOfTOK ++;
                }
            }
        }

        return numOfTOK;
    }
    
    private int checkPair(int turn , String who)
    {
        //System.out.println("checkPair");
        int numOfPair = 0;
        int check = 0;
        ArrayList<Integer> collectCard = new ArrayList<>();
        
        if(who == "player")
        { collectCard = collectCardPlayer(turn, "num"); //CollectCard Pot-Player / turn 
        } else collectCard = collectCardCom(turn, "num"); //CollectCard Pot-Com / turn 

        //CheckPair
        Collections.sort(collectCard);
            for (int i = 0; i < collectCard.size() - 1; i++) {
                if (Objects.equals(collectCard.get(i), collectCard.get(i + 1))) {
                    check++;
                } else {
                    check = 0;
                }
                
                if(check == 1)
                {
                    numOfPair++;
                }
                if(check > 1) //Do not collect ThreeOfKind Or FourOfKind
                {
                    numOfPair--;
                }

        }

        return numOfPair;
    }
        
    private int checkHandRank(int turnPlayed,String who)
    {
        System.out.println("checkHandRank");
        if(checkStraight(turnPlayed, who) == 2 && checkFlush(turnPlayed, who) == true)
        {
            return 10; //#1 Royal Straight Flush
        } else if (checkStraight(turnPlayed, who) == 1 && checkFlush(turnPlayed, who) == true)
        {
            return 9; //#2 Straight Flush
        } else if (checkFourOfKind(turnPlayed, who) == true)
        {
            return 8; //#3 Four Of Kind
        } else if (checkThreeOfKind(turnPlayed, who) == 2)
        {
            return 7; //#4 Full House (1 Three Of Kind and 1 Pair)
        } else if (checkThreeOfKind(turnPlayed, who) == 1 && checkPair(turnPlayed, who) >= 1)
        {
            return 7; //#4 Full House (1 Three Of Kind and 1 Pair)
        } else if (checkFlush(turnPlayed,who) == true)
        {
            return 6; //#5 Flush
        } else if (checkStraight(turnPlayed, who) == 1)
        {
            return 5; //#6 Straight
        } else if (checkThreeOfKind(turnPlayed, who) == 1)
        {
            return 4; //#7 Three Of Kind
        } else if (checkPair(turnPlayed, who) >= 2)
        {
            return 3; //#8 Two Pair
        } else if (checkPair(turnPlayed, who) == 1)
        {
            return 2; //#9 One Pair
        } else return 1; //High Card
        
    }
    
    private void showHandRank(int turnPlayed,String who)
    {
        System.out.println("showHandRank");
        String handRank;
        if(checkHandRank(turnPlayed, who) == 10)
        {
            handRank = "Royal Straight Flush";
        } else if(checkHandRank(turnPlayed, who) == 9)
        {
            handRank = "Straight Flush";
        } else if(checkHandRank(turnPlayed, who) == 8)
        {
            handRank = "Four Of Kind";
        } else if(checkHandRank(turnPlayed, who) == 7)
        {
            handRank = "Full House";
        }else if(checkHandRank(turnPlayed, who) == 6)
        {
            handRank = "Flush";
        }else if(checkHandRank(turnPlayed, who) == 5)
        {
            handRank = "Straight";
        }else if(checkHandRank(turnPlayed, who) == 4)
        {
            handRank = "Three Of Kind";
        }else if(checkHandRank(turnPlayed, who) == 3)
        {
            handRank = "Two Pair";
        }else if(checkHandRank(turnPlayed, who) == 2)
        {
            handRank = "One Pair";
        }else handRank = "High Card";
        
        System.out.println("end check");
        
        if(who == "player")
        {
            playerOnHand.setText(handRank);
        } else comOnHand.setText(handRank);
    }
}