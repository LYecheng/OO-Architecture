import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by LiYecheng on 04/23/15.
 */
public class Driver {

    static final double INTEREST_RATE = 0.0294;

    public static void main(String[] args) {

        MacaulayDuration macD = new MacaulayDuration();
        ModifiedDuration modD = new ModifiedDuration();

        LinkedList<Asset> portfolio0 = new LinkedList<Asset>();
        Asset myAccount = new Portfolio("myAccount", portfolio0);

        // Instantiate 3 individual bonds
        portfolio0.add(new Bond("Petrobras Global Fin", 3, 831100, 2, 0.0625, 6.25));
        portfolio0.add(new Bond("Petrobras Intl Fin", 2, 975330, 2, 0.0589, 5.38));
        portfolio0.add(new Bond("Time Warner Cable Inc", 3, 897530, 2, 0.0521, 4.5));
        // Instantiate nested portfolio1 with 3 bonds
        Portfolio portfolio1 = new Portfolio("NO1", new LinkedList<Asset>());
        portfolio1.add(new Bond("Wells Fargo & Co New Medium", 1, 120250, 2, 0.0303, 3));
        portfolio1.add(new Bond("At&t Inc T.MB", 2, 102020, 2, 0.0101, 2.95));
        portfolio1.add(new Bond("Ab Svensk Exportkredit", 1, 100704, 4, 0.0027, 1.75));
        portfolio0.add(portfolio1);
        // Instantiate nested portfolio2 with 3 bonds
        Portfolio portfolio2 = new Portfolio("NO2", new LinkedList<Asset>());
        portfolio2.add(new Bond("Petrobras Intl Fin Co", 4, 102777, 2, 0.0482, 5.88));
        portfolio2.add(new Bond("Barclays Plc", 2, 100696, 2, 0.0175, 2.0));
        portfolio2.add(new Bond("Anadarko Pete Corp", 3, 102981, 2, 0.0307, 3.45));
        portfolio0.add(portfolio2);

        // iterator: to get the number of individual bonds and nested portfolios
        Iterator<Asset> iterator =  myAccount.createAssetIterator();
        int numOfPortfolios = 0;
        int numOfBonds = 0;
        while(iterator.hasNext()){
            if(iterator.next() instanceof Bond)
                numOfBonds++;
            else
                numOfPortfolios++;
        }
        System.out.printf("I am an account and I have %d portfolios and %d individual bonds. Account MacD is %.2f and ModD is %.2f.\n", numOfPortfolios, numOfBonds, myAccount.getDuration(macD), myAccount.getDuration(modD));
        System.out.printf("The durations of the %d individual assets are: \n\n", numOfBonds+numOfPortfolios);

        // iterator2: to print the macD and modD of the Assets (Bond & Portfolio)
        Iterator<Asset> iterator2 =  myAccount.createAssetIterator();
        while(iterator2.hasNext()){
            Asset item = iterator2.next();
            item.print();
        }

    }
}
