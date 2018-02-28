
import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.Negation;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;
import java.util.ArrayList;
import java.util.List;


public class metamap {

public static void main(String[] args) throws Exception {
String input = "Life of the animal";
MetaMapApi api = new MetaMapApiImpl();
System.out.println("api instanciated");
List<String> theOptions = new ArrayList<String>();
                //String options=" -i -y -z -u -d";
                
                theOptions.add("-g");
                theOptions.add("-i");
                theOptions.add("-y");
                theOptions.add("-z");
                theOptions.add("-u");
                theOptions.add("-d");
                if(theOptions.size()>0)
                {
                    api.setOptions(theOptions);    
                }
 //api.setOptions(options);    
    List<Result> resultList = api.processCitationsFromString(input);
    Result result = resultList.get(0);
    List<Negation> negations = result.getNegationList();
    for (Negation negation : negations){
        
            System.out.println("conceptposition:" +negation.getConceptPositionList());
            System.out.println("concept pairs:" +negation.getConceptPairList());
            System.out.println("trigger positions: "+negation.getTriggerPositionList());
            System.out.println("trigger: "+negation.getTrigger());
            System.out.println("type: "+negation.getType());
       }
    for (Utterance utterance: result.getUtteranceList()) {
        System.out.println("Utterance:");
        System.out.println(" Id: " + utterance.getId());
        System.out.println(" Utterance text: " + utterance.getString());
        System.out.println(" Position: " + utterance.getPosition());
        
        for (PCM pcm: utterance.getPCMList()) {
                System.out.println("Mappings:");

                for (Mapping map: pcm.getMappingList()) {
                            System.out.println("Phrase:");
                                    System.out.println(" Map Score: " + map.getScore());
    
                                    for (Ev mapEv: map.getEvList()) {
        System.out.println(" Score: "
                + "" + mapEv.getScore());
        System.out.println(" Concept Id: " + mapEv.getConceptId());
        System.out.println(" Concept Name: " + mapEv.getConceptName());
        System.out.println(" Preferred Name: " + mapEv.getPreferredName());
        System.out.println(" Matched Words: " + mapEv.getMatchedWords());
        System.out.println(" Semantic Types: " + mapEv.getSemanticTypes());
        System.out.println(" MatchMap: " + mapEv.getMatchMap());
        System.out.println(" MatchMap alt. repr.: " + mapEv.getMatchMapList());
        System.out.println(" is Head?: " + mapEv.isHead());
        System.out.println(" is Overmatch?: " + mapEv.isOvermatch());
        System.out.println(" Sources: " + mapEv.getSources());
        System.out.println(" Positional Info: " + mapEv.getPositionalInfo());
    }
   }
}
}
}
}