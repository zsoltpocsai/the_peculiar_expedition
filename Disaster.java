import java.util.Random;

public class Disaster {

    private static final int DAMAGE = 45;

   public Disaster(Player player, int chanceOfDisaster) {
       Random rand = new Random();
       TeamMember teamMember;
       int randNum;

       if (rand.nextInt(100) < chanceOfDisaster) {

           System.out.println("A disaster happened!");

           randNum = rand.nextInt(100);

           if (!player.team.isEmpty()) {

               //Injury
               if (0 <= randNum && randNum < 20) {
                   teamMember = player.team.get(rand.nextInt(player.team.size()));
                   teamMember.getsInjured();
                   System.out.println(teamMember.getName() + " has got injured!");
                   InputHandler.printBlankInput();
               }

               //Leaving
               if (20 <= randNum && randNum < 30) {
                   teamMember = player.team.get(rand.nextInt(player.team.size()));
                   player.team.remove(teamMember);
                   System.out.println(teamMember.getName() + " has left your team!");
                   InputHandler.printBlankInput();
               }
           }

           //Damage
           if (30 <= randNum && randNum < 100) {
               player.decreaseEnergy(DAMAGE);
               for (TeamMember tm : player.team) {
                   tm.decreaseEnergy(DAMAGE);
               }
               System.out.println("You and your team suffered a serious damage!");
               InputHandler.printBlankInput();
           }
       }
   }
}
