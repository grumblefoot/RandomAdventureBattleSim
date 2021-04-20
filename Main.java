
// This is a working build! Please try it out! Hit the Run button above this line! 
// last update 1:20 PM on 8/19/2020
// this is Main build 004.0
  // build 004.0 - adds new battle system with crits & Monster story calls.

// changes in build 004.0 from 3.0:
  // removed battle number choice and went back to text inputs for attacks
  //crits are an additional if statment in battleLoop that checks if a 6 was rolled on the accuracy check. 
  // if it was, then the attack hits and 
    // deals attack (stat[0]) 
    // or magicAttack (stat[2]) in damage to the monster.
    // monsters cannot critical hit player.
  // minimum 1 damage attacks now inplimented! - both monster and player
  // added preBattle(level, mGen); which holds the preBattle story elements for each monster
  // added postBattleLost(level, mGen); w4hich holds story elements for a loss to monster
  // added postBattleWon (level, mGen); which holds story elements for a win against monster
    // level and mGen should assign the approprate monster story for all these commands
  // Choice of race & class now give stat buffs
  // fixed bugs
  // added postBattleWon story
  // added postBattleLost story
	// added preBattle story

// This is showing as error free in eclipse.
// feel free to add/change anything in this, just please update the build when you do!

// bug fixes: please list them here!
  // fixed bug where non int inputs would break gear choices.
  // fixed bug where race and class selection would crash if wrong input was given
  // fixed Player needs to input choice twice in the battleloop after the first attack 
  // fixed Player name can be set as an empty string if you hit enter/return on that field
  // fixed player name empty field issue 1030 8/19 (put in new code for fix, now I see that it was marked as fixed before I did that - jmk)

// known bugs: please list them here!
  // - Player input can be bugged in combat loop if backslash added to end of statment

// Feedback: please place it here!
  // Feedback - Rowan:
    // have not implimented mAttack or mDefense buffs/gear yet. 
    // Perhaps have existing gear grant multiple buffs to diffrent stats?
    // currently weapon/armor/gear flat buffs to simgle stat
    // many story elements inncorrect or missing



import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


// this runs the program
class Main {
	
	
	public static void main(String[] args) {
    System.out.println("build version 3.4");
	  gameLoadOrder();		
	}
	
	// this is the current load order 
	public static boolean gameLoadOrder() {
    
		System.out.println("Welcome to GAGAS!(Generic Adventure Game Adventure Stuff)");
		CharecterGen(); // this call's the inital character creation
    
		characterEquips(level, stat, s); //this calls the equip commands

		MonStat(level, mStat, b); // this calls the first monster
		preBattle(level, mGen);	//STORY HERE BEFORE FIRST BATTLE
    
		if(battleLoop(stat, mStat, b, s) == false) { // this runs the battle loop, and ends the game if you are defeated.
			postBattleLost(level, mGen);	// IF YOU LOSE TO THE FIRST MONSTER 
      System.out.println("You have died, GAME OVER");
		return false; // this causes GameOver
		}
		// IF YOU WIN AGAINST THE FIRST MONSTER 
    postBattleWon(level, mGen); // this is a post battle story call for victory   
		System.out.println("You defeat the first monster! But there are more lurking around the corner ..."); 
		levelUp(stat); // this advances the level and rewards the player with new equipment
		MonStat(level, mStat, b); // this generates a new monster
		//STORY HERE BEFORE SECOND BATTLE
		preBattle(level,mGen); // this is prebattle story call
		if(battleLoop(stat, mStat, b, s) == false) {
      // this runs the battle loop, and ends the game if you are defeated.

			postBattleLost(level, mGen);  // IF YOU LOSE TO THE SECOND MONSTER
			System.out.println("You have died, GAME OVER");
			return false;} // this causes gameover

		// If YOU WIN AGAINST THE SECOND MONSTER      
		System.out.println("You defeat the second monster! Last one coming up!");
		postBattleWon(level, mGen); // this is a post battle story call for victory  
		
		levelUp(stat); // this advances the level and rewards the player with new equipment
		MonStat(level, mStat, b); // this generates a new monster
		//STORY HERE BOSS BATTLE BEFORE BATTLE
		preBattle(level, mGen); // this is prebattle story call
		if(battleLoop(stat, mStat, b, s) == false) {
    // this runs the battle loop, and ends the game if you are defeated.
			postBattleLost(level, mGen); // IF YOU LOSE TO THE BOSS MONSTER

      System.out.println("You have died, GAME OVER");
			return false;
		}
    postBattleWon(level, mGen);


		//STORY HERE EPILOGUE 
		System.out.println("News of your exploits has reached the ruler of the land, impressed with your victories they have decided to knight you and bestow a small fortune upon you. You soon become a legendary knight across the lands. Your name will go down in history! You win the game!");
		return true;
		}
	
  	
	// this controls the gear rewards.
	public static int[] characterEquips(int level, int[] stat, Scanner s) {
		System.out.println("Please choose a weapon, armor, and 2 pieces of special gear");
		
		weaponEquip(level, stat);
		armorEquip(level, stat);
		gearACCEquip(level, stat);
		gearEVAEquip(level, stat);
		return stat;
	}
 
	// In this section, the Stats for both monsters and PC are intilized. 

	public static int attack = 0;
	public static int defense = 0;
	public static int mAttack = 0;
	public static int mDefense = 0;
	public static int evasion = 0;
	public static int accuracy = 0;
	public static int hp = 0;
	public static int level = 0;
	
	// Arrays used: "stat" is used for the player charecter stat blocks
	// "mStat" is used for the monster's stat blocks
	// "b" is used for randoms
	// "charStats is used to display the name of the stat for both monsters and PC
	
	public static int[] stat ={attack, defense, mAttack, mDefense, accuracy, evasion, hp};
	public static int[] mStat ={attack, defense, mAttack, mDefense, accuracy, evasion, hp};

	public static Random b = new Random();
	public static String[] charStats = {"Attack","Defense","Magic Attack","Magic Defense","Accuracy","Evasion","Heath Points"};
	public static String name = "";
	public static String mName = "";
	double[] playerStat = {5,5,5,5,5,5,5};
	double[] enemyStat= {6,4,4,4,6,4,5};
	static Scanner s = new Scanner(System.in);
	
	//BEGIN BATTLE LOOP
	
	public static boolean battleLoop(int stat[], int mStat[], Random r, Scanner s) {
		double damage = 0; 
		String answer = "";
		int yourTurn = 1;
		int defenseFlag = 0;
		int defence = stat[1];
		int pRoll = 0;
		int mRoll = 0;
		
    double yourHP = stat[6];
    double enemyHP = stat[6];
   
    System.out.print(s.nextLine());

    System.out.println("A " + mName + " apporaches! Prepare for battle!");
    //print your and enemy stats at beginning of battle
    
    System.out.println("Your stats are:\nattack: "+stat[0]+" defense: "+ stat[1]+" magic attack: "+stat[2]+" magic defense: "+stat[3]+
    		"\naccuracy: "+stat[4]+" evasion: "+stat[5]+" Hit points: "+stat[6]);
    
    System.out.println("Your foes stats are: \nattack: "+mStat[0]+" defense: "+ mStat[1]+" magic attack: "+mStat[2]+" magic defense: "+mStat[3] + 
    		    		"\naccuracy: "+mStat[4]+" evasion: "+ mStat[5]+" Hit points: "+mStat[6]);
		
		while(yourHP>0&&enemyHP>0) {
			
			while(yourTurn == 1) {
			System.out.println("\nchoose your attack:\n strength attack (type: 'str-atk'), magic attack (type: 'mag-atk'), defend! (type: 'def')");
	    	
			answer = s.nextLine();
			
			if(answer.equals("str-atk")) {
			pRoll = stat[4] + r.nextInt(6)+1;
			mRoll = mStat[5] + r.nextInt(6)+1;
			System.out.println(name + " attacks with an accuracy roll of " + pRoll + "!");
			System.out.println(mName + " attempts to evade with a roll of " + mRoll + "!");
				if(pRoll>=mRoll) {
					if(stat[0]- mStat[1] > 0){
						damage = stat[0]- mStat[1];
					}
					else {
						damage = 1;
					}
				if(pRoll == 6 + stat[4]) {
					damage = stat[0];
					System.out.println(name + " Scored a critical hit!" );
					
				}
					enemyHP= enemyHP-damage;
					System.out.println("You did "+damage+" damage! The " + mName +" HP is "+enemyHP+"!");
				
				}
				else {
					System.out.println("The " + mName+ " dodges your attack, you missed!");  
				}
			
			} if(answer.equals("mag-atk")) {
				pRoll = stat[4] + r.nextInt(6)+1;
				mRoll = mStat[5] + r.nextInt(6)+1;
				System.out.println(name + " attacks with an accuracy roll of " + pRoll + "!");
				System.out.println(mName + " attempts to evade with a roll of " + mRoll + "!");
				if(pRoll>=mRoll) {
					if(stat[2]-mStat[3]>0){
            damage = stat[2]-mStat[3];
          }
          else {
            damage = 1;
          }
          if(pRoll == 6 + stat[4]) {
					damage = stat[2];
					System.out.println(name + " Scored a critical spell hit!" );
					
				}
				enemyHP= enemyHP-damage;
				System.out.println("You did "+damage+" damage! The " + mName +" HP is "+enemyHP+"!");}
				
				
				else {
					System.out.println("Your spell goes hay-wire, you missed!"); 
				}
			}
			
			 if(answer.equals("def")) {
				defenseFlag++;
				defence = stat[1]+3;
				System.out.println("You defend yourself! Defense increased by 3! Your defense is " + defence);
			}
        else if(answer.contains("def")==false && answer.contains("str-atk") == false && answer.contains("mag-atk")==false) {
					System.out.println("Please provide valid input.");
          yourTurn++;
				
      }
			
			yourTurn--;}
			if(enemyHP<=0) {
				continue;
			}
			pRoll = stat[5] + r.nextInt(6)+1;
			mRoll = mStat[4] + r.nextInt(6)+1;
			System.out.println(mName + " attacks with an accuracy roll of " + mRoll + "!");
			System.out.println(name + " attempts to evade with a roll of " + pRoll + "!");
			
			if (mRoll>=pRoll) {
				if(mStat[0]- defence > 0){
        damage = mStat[0] - defence;
        }
        else {
          damage = 1;
        }
				yourHP=yourHP-damage;
				System.out.printf( mName + " did "+damage+" damage! Your HP is "+yourHP+"!");
			}
			else {
      System.out.println("You jump out of the " + mName + "'s way, "+ mName +" missed!"); 
			}
			
		
		if(defenseFlag == 1) {
			defense = stat[1];
			defenseFlag--;
			
		}
		
		yourTurn++;}
		if(yourHP<=0) {
			System.out.println(" You have been defeated, Game Over.");
			return false;
		}
		if(enemyHP<=0) {
			System.out.println(" You are triumphant! The adventure continues...");
			return true;
		
		}
		return false;
	}

	//END BATTLE LOOP
//BEGIN LEVELUP METHOD

	public static int[] levelUp(int[] stats) {

// this code adds a number between 1 and 3 to each of the player charecter's stats.
// it then outputs the new stat values

		
// if you want to grant an improved weapon, you can call "weaponEquip(level, stat);"
// this should grant a weapon appropriate to the level 
// and add that weapon's bonus to the players attack stat[0].
// other items could also be added here if we want additional rewards. 

		level++;
		System.out.println("You just leveled up!");
		
		for (int i=0; i < stats.length;i++) {
			if(i == 6) {
				stats[i]+=b.nextInt(3)+3;
			}
			else {
				stats[i] += (b.nextInt(3)+1);
			}
		}
			characterEquips(level, stat, s);
			
		
			
			
		return stats;
	}

//END LEVELUP METHOD


// Story conditionals
public static boolean postBattleWon(int level, int mGen) {
		 // Big Rat
	    if(level== 0 && mGen== 0) {

	      System.out.println("You carefully defeat the rat, doing your best not to contract any diseases.\n You attack it for the final blow and then hurry off to find hand sanitizer. You Win!.");
	    }
	      
	      // Dog-sized Spider
	      if(level== 0 && mGen== 1) { 
			
	    	  System.out.println("You kick the spider as hard as you can, sending it flying, 'huh that worked pretty well' you think, and kick it again.\n After 30 minutes of solo soccer practice you give up, and the spider lies dead. You Win!.");
	      }

	      // Goblin
	      if(level== 0 && mGen== 2) {
	      System.out.println("The Goblin screeches as it staggers backward, mortally wounded.'stupid adventurers!' it cries.\n The goblin drops some loot and falls to the ground, defeated ");
	      }

	      // Zombie
	      if(level== 0 && mGen== 3) {
	      System.out.println("The zombie groans and lunges forward as you deal a fatal blow to it's head.\n You turn to leave and feel the zombies hand grab on to your ankle, you turn around and smash the zombies head one more time, it's hand goes limp. Dang zombies smell bad. But you win!");
	      }
        
        // Rabid Dog
        if(level == 0 && mGen == 4) {

		    System.out.println("The rabid dog howls and slumps to the ground, it's mouth still foaming.\n You decide it would be a bad idea to get any closer to it than you already have, and leave it where it lies. You win!");
		}
        
        
	      // GIANT ANT
	      if(level== 1 && mGen== 0) {

	        System.out.println("The giant ant stands on it's hind legs, towering over you. As it stands, you spot it's weak spot, and attack.\n The giant ant cackles and falls over on it's back, as it's legs scrunch up together. Next time don't leave food out. You win!");
	      }
	        
	       // BLACK BEAR
	      if(level== 1 && mGen== 1) { 
	  		
	      	  System.out.println("The black bear should have stuck to honey and berries, you think, as you deal the final blow.\n Maybe it still has some honey lying around, either way, I can sell it's skin for a good price. You win!");
	      }

	        // HONEY BADGER
	      if(level== 1 && mGen== 2) {
	        System.out.println("The honey badger goes beserk, and you can't tell if it's because you wounded it or if it's always like that.\n Suddenly it stops moving, guess it was cause you wounded it! You win.");
	      }

	        // BANDIT
	       if(level== 1 && mGen== 3) {
	        System.out.println("The bandit curses your name as he slumps to the floor. 'All I wanted was your coin...' he says as he breathes his last breath.\n 'Shouldn't have pulled a weapon on me then' you think. Congrats on not becoming a crime statistic! You win!");
	        }
          
          // FLYING SHARK
      if(level== 1 && mGen== 4) {
      System.out.println("You seriously can't tell if you are winning or not because the shark just laughs at you as it flies over your head.\n 'Am I high?' you think as the shark suddenly falls to the ground, dead. 'Well if I was I'm sober now'. You win!");
      }



	        // Armored Bear
	        if(level== 2 && mGen== 0) {

	          System.out.println("This Bear is hard to damage, but you find a weak spot in the armor, and strike. The bear howls, feeling the blow.\n You strike again, and the bear falls over, dead. You can sell his armor for a pretty penny. You win!");
	        }
	          
	        // Elephant sized Spider
	        if(level== 2 && mGen== 1) { 
	    		
	        	System.out.println("You duck and dodge the spiders attacks, praying for your survival. You slash at it's leg, and score a lucky shot, it falls to the ground.\n You take advantage of the situation and attack it's head, killing it. You take a deep breath and thank your lucky stars. You win!");
	        }

	          // 30ft Snake
	          if(level== 2 && mGen== 2) {
	          System.out.println("You slash widly at the snake as it lunges at you. It almost swallows you whole but you grab the opening of it's mouth and keep it from swallowing you.\n You stab through the roof its mouth and kill it instantly. It will no longer terrorize the lands. You win!");
	          }

	          // 12ft tall Human
	          if(level== 2 && mGen== 3) {
	          System.out.println("This guy definitly ate his vegatables, but it won't be enough to defeat you!\n You trip the giant with a well placed shot, the giant topples over as you deal the final blow. His rule over the land is sure to end. You Win!");
	          }
            
	          // War Robot
	          if(level == 2 && mGen == 4) {
	        	  System.out.println("This deadly foe from the future fires his phazer to seal your fate.\n You defetly dodge and deflect his dangeours damage. You find his off switch and flick it, it falls over dead. So much for robot domination! You win!");
	          }

		return true;
	}
	public static boolean postBattleLost(int level, int mGen) {
		 // Big Rat
    if(level== 0 && mGen== 0) {

      System.out.println("The rat sees you but ignores you at first. The it smells something. Turns out you still smell like burgers from lunch.\n The rat scurries towards you, grabs on, and pulls you towards the sewer ... GAME OVER.");
    }
      
    // Dog-sized Spider
    if(level== 0 && mGen== 1) { 
		
    	System.out.println("You spot the spider first and start sneaking away with your friends, but as you're backing up you trip over a wet log.\n The spider's closing in. You freeze. It crawls up to you, it's body the size of a dining room table .. and you're done. GAME OVER.");
    }

    // Goblin
    if(level== 0 && mGen== 2) {
      
      System.out.println("After wreaking your room, the goblin turns to you. In the blink of the eye,\n the goblin leaps and lands on you, and that's the last thing you see ... GAME OVER.");
    }
    
    // Zombie
    if(level== 0 && mGen== 3) {
      
      System.out.println("The house gives you the chills. Water is dripping from the ceiling and the hum of nearby power lines sound deafening.\n You have the sense that you're being watched .. suddenly the door to the house slams shut and all hear is a shieking laugh ... GAME OVER.");
    }
    
    // Rabid Dog
    if(level == 0 && mGen == 4) {

		   System.out.println("The rabid dog sinks it's fangs into your thigh, you cry out in pain and fall to the ground.\n As you watch the the sky you think of happy memories, then the dog bites your throat, killing you. GAME OVER.");
		}
		

// LEVEL 2 MONSTERS

// GIANT ANT
    if(level== 1 && mGen== 0) {

      System.out.println("You and your friends quickly realize what you have done and run out of the science lab, however you were the last one out and you didn't lock the door.\n Now the ants are all over the school and will soon take over the town MUHAHAHAHA. GAME OVER.");
    }
      
    // BLACK BEAR
    if(level== 1 && mGen== 1) { 
		
    	System.out.println("You've got nowhere to go, but you're a gymnast hoping to receive a full ride scholarship to college so you know how to climb trees, so you get to the closest tall tree and scurry up it. D'oh!\n You watch in horror as the bear follows you up the tree, and you realized you should have paid more attention in biology class ... GAME OVER.");
      }

    // HONEY BADGER
    if(level== 1 && mGen== 2) {
      
      System.out.println("You've got nowhere to go, but you're a swimmer hoping to receive a full ride scholarship to college, so you dive into the river and start swimming.\n D'oh! You watch in horror as the honey badger follows you into the water, and you realized you should have paid more attention in biology class ... GAME OVER.");
      }

      // BANDIT
      if(level== 1 && mGen== 3) {
      System.out.println("The bandits catch on to your plan before you're able to catch them.\n They scramble out of town and since you don't have a car (because you're a kid) you can't chase after them. GAME OVER.");
      }

      // FLYING SHARK
      if(level== 1 && mGen== 4) {
      System.out.println("Late at night you decide to explore the warehouse where the movie props are stored.\n You're confused as two of the flying sharks are missing. Where could they have gone? You proceed to the dock behind the warehouse. GAME OVER.");
      }
      
	    // Armored Bear
	    if(level== 2 && mGen== 0) {

	    System.out.println("The bear hooks onto you by the hood of your sweatshirt dropping you into a open metal cavity in its stomach.\n It closes in on you and you've got nowhere to go. GAME OVER.");
	    }
	          
	        // Elephant sized Spider
	        if(level== 2 && mGen== 1) { 
	    		
	        	System.out.println("'I MUST FEED MY BABIES!' screams the elephant sized spider as it quickly begins to spin your your limp body with its web.\n You will serve as the sustanance for a new generation of elephant sized spiders. Good job. GAME OVER.");
	        }

	          // 30ft Snake
	          if(level== 2 && mGen== 2) {
	          System.out.println("Suddenly, the snake has its body wrapped around you.\n You exhale and the snake squeezes tighter, making you unable to inhale. This is the end.  GAME OVER.");
	          }

	          // 12ft tall Human
	          if(level== 2 && mGen== 3) {
	          System.out.println("The twelve foot tall humanoid towers over you swaying back and forth, arms swinging side to side.\n Suddenly its lips curl back revealing a crooked smile and gumline. It swipes you up with it's left hand and opens its mouth.  Death is upon you. GAME OVER.");
	          }
	          // War Robot
	          if(level == 2 && mGen == 4) {
	        	  System.out.println("As you lay bleeding out in the middle of the forrest, you think about all the good times you had drinking with your dwarf friends.\n You realize you never had the chance to profess your love to the dwarf maiden Davila. You die with regret. GAME OVER.");
	          }
	          return true;
	}

	
		public static boolean preBattle(int level, int mGen) {
		// BIG RAT
		if(level == 0 && mGen == 0) {

		  System.out.println("You just moved into a new place and there’s a rat that’s quite hungry since there wasn’t anyone living there,\n instead feeding off the contaminated garbage at the landfill nearby.");
		}

		// DOG-SIZED SPIDER
		if(level == 0 && mGen == 1) {

		  System.out.println("Ever since you were little everyone in your town knew not to go into the Forbiddan Forest, named after the place from the Harry Potter series.\n When you go explore there with a couple of your friends around the early afternoon, a storm suddenly rolls in and the sky starts pouring. Little do you know a spider has waken up from its sleep ...");
		}

		// GOBLIN
		if(level == 0 && mGen == 2) {

		  System.out.println("You and your friends went on a hiking adventure which brought you into some caves and you discovered a goblin in your backpack when you went back home.\n It’s wreaking havoc in your bedroom, tearing down the curtains, eating your bed, and repeatedly slamming the drawers of your dresser.");
		}

		// ZOMBIE
		if(level == 0 && mGen == 3) {

		  System.out.println("A house recently got partially demolished in your neighborhood and you and your friends want to go visit because next week they're going to bring a bulldozer and the house will be completely gone.\n And because you all are dumb kids, you decide to go at dusk ...");
		}

		// RABID DOG
		if(level == 0 && mGen == 4) {

		   System.out.println("You decide to go on a solo bicycle-backpacking trip where you cycle down the Washington and Oregon\n coasts when you pass a farmhouse and suddenly there are two dogs in the middle of the road barking at you ... ");

		}
		// GIANT ANT
				if(level == 1 && mGen == 0) {

					System.out.println("Giant ants are attacking the village you are staying in! You lead the largest ant away from the crowd and into a building, trapping it in a room.\n But that room won't hold it forever, you've got to squash the bug permanantly. Prepare for battle! ");
				}

				// BLACK BEAR 
				if(level == 1 && mGen == 1) {

					System.out.println("It’s your neighborhood black bear, except now it’s angry that none of the restaurants are open cause of COVID-19.\n You were the first person it saw and now its chasing after you!");
				}

			  // HONEY BADGER
        if(level == 1 && mGen == 2) {

        System.out.println("It’s your neighborhood honey badger, except now it’s angry that none of the restaurants are open cause of COVID-19.\n You were the first person it saw and now its chasing after you!");
        }

        // BANDIT
        if(level == 1 && mGen == 3) {

        System.out.println("You see on the news that a ton of packages in your town are being stolen by people warning red capes,\n so you decide to ride the bus with your friends to see if you can catch these bandits ... ");
        } 

        // FLYING SHARK
        if(level == 1 && mGen == 4) {

          System.out.println("Straight out of a crossover between the movies Jaws and Airplane comes a monster you've never seen before - Flying Shark.\n They decide to shoot the film in your town but little do we know this monster isn't just a prop ... ");
          }
          
	        // Armored Bear
	        if(level== 2 && mGen== 0) {

	          System.out.println("Wandering around the woods you come to the site of a great battle that has just been fought.\n Dead soliders lie on the ground everywhere. As you begin investigating you here a roar, looking up you see a survivor, an angry armored bear. Prepare for battle!");
	        }
	          
	        // Elephant sized Spider
	        if(level== 2 && mGen== 1) { 
	    		
	        	System.out.println("Someone disturbed the wrong tomb and now a giant man-eating spider is attacking the land! You take a wanted poster and go searching for the spider.\n You spot it in the woods weaving a giant web. Getting crafty, you set the web on fire, this gets the spiders attention and it attacks!");
	        }

	          // 30ft Snake
	          if(level== 2 && mGen== 2) {
	          System.out.println("A mad wizard has summoned a giant snake, which has taken over his tower. He is offering a great reward for anyone who can kill the snake.\n You enter the tower and find the snake in the caverns below the tower, it sees you and hisses angrily. Maybe this was a bad idea. Prepare for battle!");
	          }

	          // 12ft tall Human
	          if(level== 2 && mGen== 3) {
	          System.out.println("You are frolicking in a field ouside of town when the ground beneath you begins to shake.\n Is it an earthquake? You look up to see a giant 12 foot man staring down at you with eyes bulging. He says nothing. He mindlessly starts attacking you.");
	          }
	          // War Robot
	          if(level == 2 && mGen == 4) {
	        	  System.out.println("As you stroll through the forest one summer night after drinking with some dwarf friends, you can't help but feel like you being watched.\n You look down to see a red dot on your chest. 'What's this?' you think. You step to the side just in time as the shrub behind you explodes. \n'I AM HERE TO ANNIHILATE  YOU.'  You look up. You are being targeted by a War Robot!");
	          }

		return true;
	}

//BEGIN ACCURACY METHOD

	// this class generates a level appropriate Gear piece.
	// GearAccEquip(level, stat);
	// the level is a value between 0 - 2, it starts at zero,
	// please add to the level at the end of a battle
	// stat is the player's stats
	// this class inputs the player's stats, asks the player to choose a gear with a scanner
	// once a gear is selected, the class adds that gear's buff to the appropriate stat
	// it then outputs the improved stats.

	// CharecterGen & StatGen need to be in your eclipse and in the same package to enable this code. 



	    // test code for this class goes here

		//	gearACCEquip(level, stat);
	  
	// goals of this class:
	// intilize the gear options.
	// Store the bonus values as an Int[]. 
	// Store the gear name as a String[].
	// create a method to add the gear bonus to the relevant stat.
	 // i stat & mStat locations -> {0 = attack, 1 = defense, 2 = mAttack, 3 = mDefense, 4 = accuracy,  5 = evasion, 6 = hp};


	// Do we want a random value between min and max, or a set bonus? currently using set.
	// note that these ints & Strings could (should?) be moved to StatGen. 

	static int rangeFinder = 2;
	static int soundLocator = 2;
	static int thermalGoggles = 2;
			
	static int motionTracker= 1;
	static int laserRangeFinder = 1;
	static int sonar = 1;
			
	static int magicAssist = 1;
	static int valueAddedTargeter = 1;
	static int naviAssist = 1;
	 


	  // to access the 2d array, use the first bracket [] to set basic, advanced, or legend. 
	  // use the second bracket to access the gear value
	  // so weapon[0][1] is the soundLocator currently
	  // and weapon [2][1] is the valueAddedTargeter currently
	    
	public static int[][] gearAcc  = {{rangeFinder, soundLocator, thermalGoggles}, {motionTracker, laserRangeFinder, sonar},{magicAssist, valueAddedTargeter, naviAssist}};


	public static String[] basicGearAcc = {"Range Finder", "Sound Locator", "Thermal Goggles"};
	public static String[] advanceGearAcc = {"Motion Tracker", "Laser Rangefinder", "Sonar"};
	public static String[] legendaryGearAcc = {"Magic Assist", "Value Added Targeter", "Navi's Assist"};


	// if the level is zero, then generate a basic weapon
	// The player chooses the weapon with the selection of 0, 1, or 2.

	public static int[] gearACCEquip(int level, int[] stat) {
	    if(level == 0) {
	    	equip = basicGearAcc;
	    	System.out.println("Choose 0, 1, or 2");
	    	for(int i = 0; i < 3; i++ ) {
	    		System.out.println( i  + " = " + equip [i]);
	    	}

	// Player choice input is in this section. This code should only accept inputs of 0, 1, 2
	// Any other inputs should be rejected and the loop continues.

	    	System.out.println("Choose here ");
	    
	    	while(s.hasNext()) {   		
	    		if(s.hasNextInt()) {
	    			playerChoice = s.nextInt();	
	    				System.out.println("Player chose " + playerChoice);
	    				if(playerChoice == 0 || playerChoice == 1 || playerChoice == 2) {
	    					System.out.println("equipped " + equip [playerChoice]);
	    					break;
	    				}
	    				else {
	    					System.out.println("Not a valid choice!");
	    				}
	    			}
	    		else if(!s.hasNextInt()) {
	    			String junk = s.next();
	    			System.out.println( junk + "Not a valid choice!");
	    		}
	    		}
	    		
	// this adds the armor bonus to the defense stat. 

	    		stat[4] = stat[4] + gearAcc[0][playerChoice];
	    		System.out.println("Player chose " + equip [playerChoice] + " This sets " + charStats[4] + " to " + stat[4]);
	    		return stat;
	    }	

	// if the level is 1, generate an advanced gear.

	    if(level == 1) {
	    	equip = advanceGearAcc;
	    	System.out.println("Choose 0, 1, or 2");
	    	for(int i = 0; i < 3; i++ ) {
	    		System.out.println( i  + " = " + equip [i]);
	    	}
	      
	      // Player choice input is in this section.
	      // This code should only accept inputs of 0, 1, 2
	      // Any other inputs should be rejected and the loop continues.
	    
	    		System.out.println("Choose here ");
	    		
	    		while(s.hasNext()) {   		
	    			if(s.hasNextInt()) {
	    				playerChoice = s.nextInt();	
	    				System.out.println("Player chose " + playerChoice);
	    				if(playerChoice == 0 || playerChoice == 1 || playerChoice == 2) {
	    					System.out.println("equipped " + equip [playerChoice]);
	    					break;
	    				}
	    				else {
	    					System.out.println("Not a valid choice!");
	    				}
	    			}
	    			else if(!s.hasNextInt()) {
		    			String junk = s.next();
		    			System.out.println( junk + "Not a valid choice!");
		    		}
	    		}
	    		
	    		stat[4] = stat[4] + gearAcc[1][playerChoice];
	    		System.out.println("Player chose " + equip [playerChoice] + " This sets " + charStats[4] + " to " + stat[4]);
	    		return stat;
	    }

	// if the level is 2, generate a legend weapon

	    if(level == 2) {
	    	equip = legendaryGearAcc;
	    	System.out.println("Choose 0, 1, or 2");
	    	for(int i = 0; i < 3; i++ ) {
	    		System.out.println( i  + " = " + equip [i]);
	    	}

	      // Player choice input is in this section.
	      // This code should only accept inputs of 0, 1, 2
	      // Any other inputs should be rejected and the loop continues.
	    
	    		System.out.println("Choose here ");
	    	
	    		while(s.hasNext()) {   		
	    			if(s.hasNextInt()) {
	    				playerChoice = s.nextInt();	
	    				System.out.println("Player chose " + playerChoice);
	    				if(playerChoice == 0 || playerChoice == 1 || playerChoice == 2) {
	    					System.out.println("equipped " + equip [playerChoice]);
	    					break;
	    				}
	    				else {
	    					System.out.println("Not a valid choice!");
	    				}
	    			}
	    			else if(!s.hasNextInt()) {
		    			String junk = s.next();
		    			System.out.println( junk + "Not a valid choice!");
		    		}
	    		}
	    		
	    		stat[4] = stat[4] + gearAcc[2][playerChoice];
	    		System.out.println("Player chose " + equip [playerChoice] + " This sets " + charStats[4] + " to " + stat[4]);
	    		return stat;
	    }
	    

	    // and the weapon value should be added to the attack Stat (stat[0])
	    // this code could be modified to change any stat as needed to make other items/weapons.
	    // the output of this code is the Stat array + weapon buff.

	    return stat;
	  }

	
	  
//END ACCURACY METHOD 





//BEGIN EVASION METHOD

// this class generates a level appropriate Gear piece.
// GearAccEquip(level, stat);
// the level is a value between 0 - 2, it starts at zero,
// please add to the level at the end of a battle
// stat is the player's stats
// this class inputs the player's stats, asks the player to choose a gear with a scanner
// once a gear is selected, the class adds that gear's buff to the appropriate stat
// it then outputs the improved stats.

// CharecterGen & StatGen need to be in your eclipse and in the same package to enable this code. 


  


    // test code for this class goes here

	//	gearEVAEquip(level, stat);
  
// goals of this class:
// intilize the gear options.
// Store the bonus values as an Int[]. 
// Store the gear name as a String[].
// create a method to add the gear bonus to the relevant stat.
 // i stat & mStat locations -> {0 = attack, 1 = defense, 2 = mAttack, 3 = mDefense, 4 = accuracy,  5 = evasion, 6 = hp};


// Do we want a random value between min and max, or a set bonus? currently using set.
// note that these ints & Strings could (should?) be moved to StatGen. 

static int camoCloak = 2;
static int soundDamper = 2;
static int scentBlocker = 2;
		
static int distortionBelt = 1;
static int phantomSound = 1;
static int deployableDecoy = 1;
		
static int illusionGenerator = 1;
static int destractionBird = 1;
static int displacementField = 1;

  // to access the 2d array, use the first bracket [] to set basic, advanced, or legend. 
  // use the second bracket to access the gear value
  // so weapon[0][1] is the soundLocator currently
  // and weapon [2][1] is the valueAddedTargeter currently
    
public static int[][] gearEva  = {{camoCloak, soundDamper, scentBlocker}, {distortionBelt, phantomSound, deployableDecoy},{illusionGenerator, destractionBird, displacementField}};


public static String[] basicGearEva = {"Camo Cloak", "Sound Damper", "Scent Blocker"};
public static String[] advanceGearEva = {"Distortion Belt", "Phantom Sound", "Deployable Decoy"};
public static String[] legendaryGearEva = {"Illusion Generator", "Distracting Bird Ally", "Displacement Field"};


// if the level is zero, then generate a basic weapon
// The player chooses the weapon with the selection of 0, 1, or 2.

  public static int[] gearEVAEquip(int level, int[] stat) {
    if(level == 0) {
    	equip = basicGearEva;
    	System.out.println("Choose 0, 1, or 2");
    	for(int i = 0; i < 3; i++ ) {
    		System.out.println( i  + " = " + equip [i]);
    	}

// Player choice input is in this section. This code should only accept inputs of 0, 1, 2
// Any other inputs should be rejected and the loop continues.

    		System.out.println("Choose here ");
    
    		while(s.hasNext()) {   		
    			if(s.hasNextInt()) {
    				playerChoice = s.nextInt();	
    				System.out.println("Player chose " + playerChoice);
    				if(playerChoice == 0 || playerChoice == 1 || playerChoice == 2) {
    					System.out.println("equipped " + equip [playerChoice]);
    					break;
    				}
    				else {
    					System.out.println("Not a valid choice!");
    				}
    			}
    			else if(!s.hasNextInt()) {
	    			String junk = s.next();
	    			System.out.println( junk + "Not a valid choice!");
	    		}
    		}
    		
// this adds the gear bonus to the defense stat. 

    		stat[5] = stat[5] + gearEva[0][playerChoice];
    		System.out.println("Player chose " + equip [playerChoice] + " This sets " + charStats[5] + " to " + stat[5]);
    		return stat;
    }	

// if the level is 1, generate an advanced weapon.

    if(level == 1) {
    	equip = advanceGearEva;
    	System.out.println("Choose 0, 1, or 2");
    	for(int i = 0; i < 3; i++ ) {
    		System.out.println( i  + " = " + equip [i]);
    	}
      
      // Player choice input is in this section.
      // This code should only accept inputs of 0, 1, 2
      // Any other inputs should be rejected and the loop continues.
    
    		System.out.println("Choose here ");
    
    		while(s.hasNext()) {   		
    			if(s.hasNextInt()) {
    				playerChoice = s.nextInt();	
    				System.out.println("Player chose " + playerChoice);
    				if(playerChoice == 0 || playerChoice == 1 || playerChoice == 2) {
    					System.out.println("equipped " + equip [playerChoice]);
    					break;
    				}
    				else {
    					System.out.println("Not a valid choice!");
    				}
    			}
    			else if(!s.hasNextInt()) {
	    			String junk = s.next();
	    			System.out.println( junk + "Not a valid choice!");
	    		}
    		}
    		
    		stat[5] = stat[5] + gearEva[1][playerChoice];
    		System.out.println("Player chose " + equip [playerChoice] + " This sets " + charStats[5] + " to " + stat[5]);
    		return stat;
    }

// if the level is 2, generate a legend weapon

    if(level == 2) {
    	equip = legendaryGearEva;
    	System.out.println("Choose 0, 1, or 2");
    	for(int i = 0; i < 3; i++ ) {
    		System.out.println( i  + " = " + equip [i]);
    	}

      // Player choice input is in this section.
      // This code should only accept inputs of 0, 1, 2
      // Any other inputs should be rejected and the loop continues.
    
    		System.out.println("Choose here ");
    	
    		while(s.hasNext()) {   		
    			if(s.hasNextInt()) {
    				playerChoice = s.nextInt();	
    				System.out.println("Player chose " + playerChoice);
    				if(playerChoice == 0 || playerChoice == 1 || playerChoice == 2) {
    					System.out.println("equipped " + equip [playerChoice]);
    					break;
    				}
    				else {
    					System.out.println("Not a valid choice!");
    				}
    			}
    			else if(!s.hasNextInt()) {
	    			String junk = s.next();
	    			System.out.println( junk + "Not a valid choice!");
	    		}
    		}
    		
    		stat[5] = stat[5] + gearEva[2][playerChoice];
    		System.out.println("Player chose " + equip [playerChoice] + " This sets " + charStats[5] + " to " + stat[5]);
    		return stat;
    }
    

    // and the weapon value should be added to the attack Stat (stat[0])
    // this code could be modified to change any stat as needed to make other items/weapons.
    // the output of this code is the Stat array + weapon buff.

    return stat;
  }


  

//END EVASION METHOD

//BEGIN ARMOR CHOICE

// this class generates a level aproprate Armor.
// armorEquip(level, stat);
// the level is a value between 0 - 2, it starts at zero,
// please add to the level at the end of a battle
// stat is the player's stats
// this class inputs the player's stats, asks the player to choose a armor with a scanner
// once a armor is selected, the class adds that armor's buff to the approprate stat
// it then outputs the improved stats.

// CharecterGen & StatGen need to be in your eclipse and in the same package to enable this code. 



    // test code for this class goes here

		// armorEquip(level, stat);
  
// goals of this class:
// intilize the armor options.
// Store the bonus values as an Int[]. 
// Store the armor name as a String[].
// create a method to add the armor bonus to the relevant stat.
 // i stat & mStat locations -> {0 = attack, 1 = defense, 2 = mAttack, 3 = mDefense, 4 = evasion,  5 = accuracy, 6 = hp};


// Do we want a random value between min and max, or a set bonus? currently using set.
// note that these ints & Strings could (should?) be moved to StatGen. 

static int scalemail = 2;
static int chainmail = 2;
static int kevlar = 2;
		
static int steelSheild= 1;
static int woodenSheild = 1;
static int riotSheild = 1;
		
static int magicBarrier = 1;
static int forceField = 1;
static int airSheild = 1;

  // to access the 2d array, use the first bracket [] to set basic, advanced, or legend. 
  // use the second bracket to access the weapon value
  // so weapon[0][1] is the chainmail currently
  // and weapon [2][1] is the forceField currently
    
public static int[][] armor  = {{scalemail, chainmail, kevlar}, {steelSheild, woodenSheild, riotSheild},{magicBarrier, forceField, airSheild}};


public static String[] basicArmor = {"Scalemail", "Chainmail", "Kevlar Bodyglove"};
public static String[] advanceArmor = {"Steel Sheild", "Wooden Sheild", "Riot Sheild"};
public static String[] legendaryArmor = {"Magic Barrier", "Force Field", "Air Sheild"};


// if the level is zero, then generate a basic weapon
// The player chooses the weapon with the selection of 0, 1, or 2.

  public static int[] armorEquip(int level, int[] stat) {
    if(level == 0) {
    	equip = basicArmor;
    	System.out.println("Choose 0, 1, or 2");
    	for(int i = 0; i < 3; i++ ) {
    		System.out.println( i  + " = " + equip [i]);
    	}

// Player choice input is in this section. This code should only accept inputs of 0, 1, 2
// Any other inputs should be rejected and the loop continues.

    		System.out.println("Choose here ");
    	
    		while(s.hasNext()) {   		
    			if(s.hasNextInt()) {
    				playerChoice = s.nextInt();	
    				System.out.println("Player chose " + playerChoice);
    				if(playerChoice == 0 || playerChoice == 1 || playerChoice == 2) {
    					System.out.println("equipped " + equip [playerChoice]);
    					break;
    				}
    				else {
    					System.out.println("Not a valid choice!");
    				}
    			}
    			else if(!s.hasNextInt()) {
	    			String junk = s.next();
	    			System.out.println( junk + "Not a valid choice!");
	    		}
    		}
    		
// this adds the armor bonus to the defense stat. 

    		stat[1] = stat[1] + armor[0][playerChoice];
    		System.out.println("Player chose " + equip [playerChoice] + " This sets " + charStats[1] + " to " + stat[1]);
    		return stat;
    }	

// if the level is 1, generate an advanced weapon.

    if(level == 1) {
    	equip = advanceArmor;
    	System.out.println("Choose 0, 1, or 2");
    	for(int i = 0; i < 3; i++ ) {
    		System.out.println( i  + " = " + equip [i]);
    	}
      
      // Player choice input is in this section.
      // This code should only accept inputs of 0, 1, 2
      // Any other inputs should be rejected and the loop continues.
    
    		System.out.println("Choose here ");
    	
    		while(s.hasNext()) {   		
    			if(s.hasNextInt()) {
    				playerChoice = s.nextInt();	
    				System.out.println("Player chose " + playerChoice);
    				if(playerChoice == 0 || playerChoice == 1 || playerChoice == 2) {
    					System.out.println("equipped " + equip [playerChoice]);
    					break;
    				}
    				else {
    					System.out.println("Not a valid choice!");
    				}
    			}
    			else if(!s.hasNextInt()) {
	    			String junk = s.next();
	    			System.out.println( junk + "Not a valid choice!");
	    		}
    		}
    		
    		stat[1] = stat[1] + armor[1][playerChoice];
    		System.out.println("Player chose " + equip [playerChoice] + " This sets " + charStats[1] + " to " + stat[1]);
    		return stat;
    }

// if the level is 2, generate a legend weapon

    if(level == 2) {
    	equip = legendaryArmor;
    	System.out.println("Choose 0, 1, or 2");
    	for(int i = 0; i < 3; i++ ) {
    		System.out.println( i  + " = " + equip [i]);
    	}

      // Player choice input is in this section.
      // This code should only accept inputs of 0, 1, 2
      // Any other inputs should be rejected and the loop continues.
    
    		System.out.println("Choose here ");
    		while(s.hasNext()) {   		
    			if(s.hasNextInt()) {
    				playerChoice = s.nextInt();	
    				System.out.println("Player chose " + playerChoice);
    				if(playerChoice == 0 || playerChoice == 1 || playerChoice == 2) {
    					System.out.println("equipped " + equip [playerChoice]);
    					break;
    				}
    				else {
    					System.out.println("Not a valid choice!");
    				}
    			}
    			else if(!s.hasNextInt()) {
	    			String junk = s.next();
	    			System.out.println( junk + "Not a valid choice!");
	    		}
    		}
    		
    		stat[1] = stat[1] + armor[2][playerChoice];
    		System.out.println("Player chose " + equip [playerChoice] + " This sets " + charStats[1] + " to " + stat[1]);
    		return stat;
    }

    // and the weapon value should be added to the attack Stat (stat[0])
    // this code could be modified to change any stat as needed to make other items/weapons.
    // the output of this code is the Stat array + weapon buff.

    return stat;
  }


  //END ARMOR CHOICE

//BEGIN WEAPON CHOICE

// this class generates a level aproprate weapon.
// weaponEquip(level, stat);
// the level is a value between 0 - 2, it starts at zero,
// please add to the level at the end of a battle
// stat is the player's stats
// this class inputs the player's stats, asks the player to choose a weapon with a scanner
// once a weapon is selected, the class adds that weapon's buff to the approprate stat
// it then outputs the improved stats.

// CharecterGen & StatGen need to be in your eclipse and in the same package to enable this code. 




    // test code for this class goes here

		// weaponEquip(level, stat);
  
  // goals of this class:
// intilize the weapon options.
// Store the bonus values as an Int[]. 
// Store the weapon name as a String[].
// create a method to add the weapon bonus to the relevant stat.
 // i stat & mStat locations -> {0 = attack, 1 = defense, 2 = mAttack, 3 = mDefense, 4 = evasion,  5 = accuracy, 6 = hp};


// Do we want a random value between min and max, or a set bonus? currently using set.
// note that these ints & Strings could (should?) be moved to StatGen. 

static int shortSword = 2;
static int gun = 2;
static int axe = 2;
		
static int longSword = 1;
static int mace = 1;
static int spector = 1;
		
static int fireBlast = 1;
static int wand = 1;
static int execute = 1;
 

  // to access the 2d array, use the first bracket [] to set basic, advanced, or legend. 
  // use the second bracket to access the weapon value
  // so weapon[0][1] is the shortSword currently
  // and weapon [2][1] is the fireBlast currently
    
public static int[][] weapon  = {{shortSword, gun, axe}, {longSword, mace, spector},{fireBlast, wand, execute}};


public static String[] basicWeapons = {"Short Sword", "Gun", "Axe"};
public static String[] advanceWeapons = {"Long Sword", "Mace", "Spectre"};
public static String[] legendaryWeapons = {"Fire Blast", "Magical Wand", "Execute"};
public static String[] equip = {"", "", ""};
public static int playerChoice = 0;

// if the level is zero, then generate a basic weapon
// The player chooses the weapon with the selection of 0, 1, or 2.

  public static int[] weaponEquip(int level, int[] stat) {
    if(level == 0) {
    	equip = basicWeapons;
    	System.out.println("Choose 0, 1, or 2");
    	for(int i = 0; i < 3; i++ ) {
    		System.out.println( i  + " = " + equip [i]);
    	}

// Player choice input is in this section. This code should only accept inputs of 0, 1, 2
// Any other inputs should be rejected and the loop continues.

    		System.out.println("Choose here ");
    		
    		while(s.hasNext()) {   		
    			if(s.hasNextInt()) {
    				playerChoice = s.nextInt();	
    				System.out.println("Player chose " + playerChoice);
    				if(playerChoice == 0 || playerChoice == 1 || playerChoice == 2) {
    					System.out.println("equiped " + equip [playerChoice]);
    					break;
    				}
    				else {
    					System.out.println("Not a valid choice!");
    				}
    			}
    			else if(!s.hasNextInt()) {
	    			String junk = s.next();
	    			System.out.println( junk + "Not a valid choice!");
	    		}
    		}
    		
// this adds the weapon bonus to the attack stat. 

    		stat[0] = stat[0] + weapon[0][playerChoice];
    		System.out.println("Player chose " + equip [playerChoice] + " This sets " + charStats[0] + " to " + stat[0]);
    		return stat;
    }	

// if the level is 1, generate an advanced weapon.

    if(level == 1) {
    	equip = advanceWeapons;
    	System.out.println("Choose 0, 1, or 2");
    	for(int i = 0; i < 3; i++ ) {
    		System.out.println( i  + " = " + equip [i]);
    	}
      
      // Player choice input is in this section.
      // This code should only accept inputs of 0, 1, 2
      // Any other inputs should be rejected and the loop continues.
    
    		System.out.println("Choose here ");
    		while(s.hasNext()) {   		
    			if(s.hasNextInt()) {
    				playerChoice = s.nextInt();	
    				System.out.println("Player chose " + playerChoice);
    				if(playerChoice == 0 || playerChoice == 1 || playerChoice == 2) {
    					System.out.println("equiped " + equip [playerChoice]);
    					break;
    				}
    				else {
    					System.out.println("Not a valid choice!");
    				}
    			}
    			else if(!s.hasNextInt()) {
	    			String junk = s.next();
	    			System.out.println( junk + " is not a valid choice!");
	    		}
    		}
    		
    		stat[0] = stat[0] + weapon[1][playerChoice];
    		System.out.println("Player chose " + equip [playerChoice] + " This sets " + charStats[0] + " to " + stat[0]);
    		return stat;
    }

// if the level is 2, generate a legend weapon

    if(level == 2) {
    	equip = legendaryWeapons;
    	System.out.println("Choose 0, 1, or 2");
    	for(int i = 0; i < 3; i++ ) {
    		System.out.println( i  + " = " + equip [i]);
    	}

      // Player choice input is in this section.
      // This code should only accept inputs of 0, 1, 2
      // Any other inputs should be rejected and the loop continues.
    
    		System.out.println("Choose here ");
    
    		while(s.hasNext()) {   		
    			if(s.hasNextInt()) {
    				playerChoice = s.nextInt();	
    				System.out.println("Player chose " + playerChoice);
    				if(playerChoice == 0 || playerChoice == 1 || playerChoice == 2) {
    					System.out.println("equipped " + equip [playerChoice]);
    					break;
    				}
    				else {
    					System.out.println("Not a valid choice!");
    				}
    			}
    			else if(!s.hasNextInt()) {
	    			String junk = s.next();
	    			System.out.println( junk + "Not a valid choice!");
	    		}
    		}
    		
    		stat[0] = stat[0] + weapon[2][playerChoice];
    		System.out.println("Player chose " + equip [playerChoice] + " This sets " + charStats[0] + " to " + stat[0]);
    		return stat;
    }
   

    // and the weapon value should be added to the attack Stat (stat[0])
    // this code could be modified to change any stat as needed to make other items/weapons.
    // the output of this code is the Stat array + weapon buff.

    return stat;
  }


  //END WEAPON CHOICE

  //BEGIN MONSTER GENERATOR 

  // this class generates a monster when called
// MonStat(level, mStat, b); 
// the level is a value between 0 - 2, it starts at zero,
// please add to the level at the end of a battle
// mStat is the monster stats. 
// They are input at zero, and output at a random number between 1-6/2-7/3-8.
// the higher values for monster stats are generated as level increases
// b is a randomizer used for generating the monster stats 
// b is also used for mGen, this randmizes the monster called on from the list.
// mGen is also useful for matching a monster to a discription. 
// See the Monster story comments at the top of that page for more information

// This class needs the information stored in StatGen to run. 
// In Eclipse, the "extend StatGen" lets this class access the data held in StatGen to run



	  // test inputs below
		// level default is 0
		// MonStat(level, mStat, b);
		// MonStat(1, mStat, b);
		// MonStat(2, mStat, b);

		  
	// Monster names below. Please have the same number of entries for each option
	public static String[] t1Monsters = {"Big Rat", "Dog-Sized Spider", "Goblin", "Zombie", "Rabid Dog"};
	public static String[] t2Monsters = {"Giant Ant", "Black Bear", "Honey Badger", "Bandit", "Flying Shark"};
	public static String[] t3Monsters = {"Armored Bear", "Elephant sized Spider", "30ft Snake", "12ft tall Human", "War Robot"};


	  // The t1Monsters length determines number of name options for the monster names in the loop
    
	  public static int mGen = b.nextInt(t1Monsters.length);

	  // each MonStat should generate a single random monster when called on.
	public static int[] MonStat (int level, int[] mStat, Random r) {
		if(level == 0) {
			for(int m = 0; m < 1; m++) {

			// this generates the monsters Stats.

				for(int i = 0; i < mStat.length; i++){
					if(i == 6) {
						mStat[i] = (r.nextInt(6)+5);
					}
					else {
						mStat[i]= (r.nextInt(6)+2);
					}
				
					mName = t1Monsters[mGen];
				}	
			}
		}
		if(level == 1) {
			for(int m = 0; m < 1; m++) {
				for(int i = 0; i < mStat.length; i++){
					if(i == 6) {
						mStat[i] = (r.nextInt(6)+10);
					}
					else {
						mStat[i]= (r.nextInt(6)+4);
					}
					
					mName = t2Monsters[mGen];
				}	
			}
		}
		if(level == 2) {
			for(int m =0; m < 1; m++) {
				for(int i = 0; i < mStat.length; i++){
					if(i == 6) {
						mStat[i] = (r.nextInt(6)+20);
					}
					else {
						mStat[i]= (r.nextInt(6)+8);
					}
					
					mName = t3Monsters[mGen];
				}	
			}
		}
	return mStat;	
	}

//END MONSTER GENERATOR 

//BEGIN CHARACTER GENERATOR AND CHOICE


	// this is where a Character is generated. 
	// Note from Rowan: this section had some errors in is, so it has been commented out for now. 

	// String[] raceList = {"human", "elf", "dwarf", "orc"};

	// This command takes the input from the "stat" array in StatGen 
	// It uses the randmizer "b" to generate a stat value at that loction between
	// 1 and 6. It then outputs those stats with the descriptor String array
	// This output is shown as a println. 
	// The class returns the Int array "stat" with these new values.
	
	public static String CharecterGen() {
	
	String[] placeList = {"in a small town in the desert", 
			"in a cabin on the plains of Rutiarum", 
			"in a large bustling city in the North", 
			"fishing with the fleet on the Winding Sea", 
			"traveling from town to town with a troupe of jongleurs",
			"among the nomads of the sunless wastes",
			"studying in the Great Libraries of Arn",
			"working in the silver mines of the White Mountain",
			"stealing whatever they could from the nobles of Veriatas"};
	String[] houseList = {"home", "tent", "yurt", "wagon", "insula", "hovel", "cottage", "rented room"};
	String[] ocurrance = {"someone had stolen their life savings",
			"a fire had destroyed everything they owned",
			"their family had been murdered by traveling brigands",
			"a letter had arrived announcing that they had inherited a fortune", 
			"someone had left a mysterious note warning them to flee", 
			"a wild beast had eaten all their roommates"};
	
	String randomPlace = placeList[(int)(Math.random() * placeList.length)];
	String randomHouse = houseList[(int)(Math.random() * houseList.length)];
	String randomOcurrance = ocurrance[(int)(Math.random() * ocurrance.length)];
	
	
	String name = "";
	name = playerName(s);

	System.out.println(name + "'s stats are:" );
	mainStat(stat,b);
	
	String raceChoice = "";
	raceChoice = playerRace(s);

	String classChoice = "";
	classChoice = playerClass(s);
	
	
	System.out.println(name + " is a " + raceChoice + " " + classChoice + "\n");
	
	
		
	
	System.out.println(name + "'s youth was spent " + randomPlace +"." );
	System.out.println("One afternoon, " + name + " returned to their " + randomHouse + " and found that " + randomOcurrance + "."); 
	System.out.println("\"That's it!\", they thought. \"I'm leaving this place!\"");
	System.out.println(name + " quickly packed a bag and ventured forth to seek adventure.");
	return name;
	}

	

	// this Generates the player name with a scanner input when called on. The name is stored in the string name.

	public static String playerName(Scanner s){
		System.out.println("What is your name?");
		while(s.hasNext()) {
		String nameChosen = "";
		 nameChosen = s.next();
			if(nameChosen.length() < 0) {
				System.out.println("Name field cannot be blank!");
			}
			else {
				System.out.println("Your character's name is " + nameChosen + "\n");
				name = nameChosen;	
				break;
			}
			
		}
		

		return name;
	}

	// this allows the player to choose a race
	public static String playerRace(Scanner s) {
		String playerRace = "";
		System.out.println("Please type a number from 1-4 to choose your character's race:");
		System.out.println("1 - Human");
		System.out.println("2 - Elf");
		System.out.println("3 - Dwarf");
		System.out.println("4 - Orc");
		while(s.hasNext()) {
			if(s.hasNextInt()) {
				int raceChoice = s.nextInt();
				if(raceChoice == 1) {
					playerRace = "Human";
					stat[4] = stat[4] + 1;
					stat[1] = stat[1] +1;
					break;
			
				}
				else if (raceChoice == 2) {
					playerRace = "Elf";
					stat[2] = stat[2] + 1;
					stat[5] = stat[5] + 1;
					break;
				}
				else if (raceChoice == 3) {
					playerRace = "Dwarf";
					stat[1] = stat[1] + 1;
					stat[3] = stat[3] + 1;
					break;
				}
				else if (raceChoice == 4) {
					playerRace = "Orc";
					stat[0] = stat[0] + 1;
					stat[6] = stat[6] + 1; 
					break;
				}
				else {
					System.out.println("You must enter a number from 1-4");
				
				}
			}
			else if(!s.hasNextInt()) {
				System.out.println("You must enter a number from 1-4");
				String junk = s.next();
			}
			
			playerRace = playerRace(s);
		}
		
		return playerRace;	
}
	
	// this allows the player to choose a class
	public static String playerClass(Scanner s) {
		String playerClass = "";
		System.out.println("Please type a number from 1-4 to choose your character's class:");
		System.out.println("1 - Warrior");
		System.out.println("2 - Rogue");
		System.out.println("3 - Cleric");
		System.out.println("4 - Mage");
		while(s.hasNext()) {
			if(s.hasNextInt()) {
				int classChoice = s.nextInt();
				if(classChoice == 1) {
					playerClass = "Warrior";
					stat[0] = stat[0] + 1;
					stat[1] = stat[1] + 1;
					
					break;
				}
				else if (classChoice == 2) {
					playerClass = "Rogue";
					stat[4] = stat[4] + 1;
					stat[5] = stat[5] + 1;
					break;
				}
				else if (classChoice == 3) {
					playerClass = "Cleric";
					stat[1] = stat[1] + 1;
					stat[3] = stat[3] + 1;
					break;
				}
				else if (classChoice == 4) {
					playerClass = "Mage";
					stat[3] = stat[3] + 1;
					stat[2] = stat[2] + 1;
					// Code for choice goes here
					break;
				}
				else {
					System.out.println("You must enter a number from 1-4");
				}
			}
			else if(!s.hasNextInt()) {
				System.out.println("You must enter a number from 1-4");
				String junk = s.next();
			}
			playerClass = playerClass(s);
		}
		return playerClass;	
}


// this Randomizes the stats in StatGen, when called on.
public static int[] mainStat(int[] stat, Random r){

	for(int i = 0; i<stat.length;i++){
		if(i == 6){
			stat[i]=r.nextInt(5)+5;
		}
		else {
			stat[i]=r.nextInt(6)+1;
		}
		System.out.printf(name + " stat " + charStats[i] + " value is " + stat[i]+"\n");
	}

	return stat;

}

}

//END CHARACTER GENERATOR AND CHOICE

//METHOD LIST:

//battle loop
//character generator and choice
//weapon choice
//armor choice
//gear choice
//monster generator 
//level up

//GAME OUTLINE
//Story/game introduction (println)
//character generation method
//character choice method
//weapon choice method 
//armor choice method 
// story println-------------
//monster generator method 1
// BATTLE loop method 1__________
//level up method
//story println------------
//weaponchoice method
//armor choice method
//gear choice method
//monsterGEN method 2
// BATTLE loop method 2__________
// level up method
//story println------------
//weapon choice
//armor choice
//gear choice
//monsterGEN 3
//BATTLE loop 3____________
//Final Story println---------