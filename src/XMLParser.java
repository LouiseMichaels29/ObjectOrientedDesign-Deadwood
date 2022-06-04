import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

public class XMLParser {

	public HashMap<String, Room> roomObjects = new HashMap<String, Room>();
	public ArrayList<Scene> cards = new ArrayList<Scene>();
	
    public Document getDocFromFile(String filename)
            throws ParserConfigurationException{
        {
        	
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = null;

            try{
                doc = db.parse(filename);
            } catch (Exception ex){
                System.out.println("XML parse failure"); 
                ex.printStackTrace();
            }
            return doc;
        } 
    }

    public ArrayList<Room> readBoardData(Document d){

        Element root = d.getDocumentElement();
        ArrayList<Room> rooms = new ArrayList<>();

        NodeList setRooms = root.getElementsByTagName("set");
        NodeList trailerRoom = root.getElementsByTagName("trailer");
        NodeList officeRoom = root.getElementsByTagName("office");

        for(int i = 0; i < setRooms.getLength(); i++){
        	
            //Parameters to create new Room.java objects out of this data.
            String name;
            int numTakes = 0;
            ArrayList<String> neighbors = new ArrayList<>(); 
            ArrayList<Role> roles = new ArrayList<>();
            ArrayList<TakeObject> takeObjects = new ArrayList<TakeObject>();
            ArrayList<ExtraPartObject> extraPartList = new ArrayList<ExtraPartObject>();
            
            int roomX = 0;
            int roomY = 0;
            int roomH = 0;
            int roomW = 0;

            Node setRoom = setRooms.item(i);

            name = setRoom.getAttributes().getNamedItem("name").getNodeValue();

            NodeList children = setRoom.getChildNodes(); 
            for (int j = 0; j < children.getLength(); j++) {

                Node sub = children.item(j);
        
                if("neighbors".equals(sub.getNodeName())){
                    NodeList neighborsNodes = sub.getChildNodes();

                    //Iterate through each neighboring Room to this room.
                    for(int k = 0; k < neighborsNodes.getLength(); k++){
                        Node neighborsNode = neighborsNodes.item(k);
                        
                        if("neighbor".equals(neighborsNode.getNodeName())){
                            neighbors.add(neighborsNode.getAttributes().getNamedItem("name").getNodeValue());
                            //System.out.println(neighborsNode.getAttributes().getNamedItem("name").getNodeValue());
                        } 
                    }
                }
                
                else if("area".equals(sub.getNodeName())) {
                
                	roomX = Integer.parseInt(sub.getAttributes().getNamedItem("x").getNodeValue());
                	roomY = Integer.parseInt(sub.getAttributes().getNamedItem("y").getNodeValue());
                	roomH = Integer.parseInt(sub.getAttributes().getNamedItem("h").getNodeValue());
                	roomW = Integer.parseInt(sub.getAttributes().getNamedItem("w").getNodeValue());
                }
                
                //Find number of takes. 
                else if("takes".equals(sub.getNodeName())){ 
                	
                	int takeX = 0;
                    int takeY = 0;
                    int takeH = 0;
                    int takeW = 0;
                    int takeN = 0;
                    
                    NodeList takesNodes = sub.getChildNodes();

                    for(int k = 0; k < takesNodes.getLength(); k++){ 
                    	
                        Node takesElement = takesNodes.item(k);

                        if("take".equals(takesElement.getNodeName())){
                        	
                        	NodeList takesList = takesElement.getChildNodes();
                        	
                        	for(int a = 0; a < takesList.getLength(); a++) {

                        		Node takesArea = takesList.item(a);
                        		takeN = Integer.parseInt(takesElement.getAttributes().getNamedItem("number").getNodeValue());
                        		takeX = Integer.parseInt(takesArea.getAttributes().getNamedItem("x").getNodeValue());
                            	takeY = Integer.parseInt(takesArea.getAttributes().getNamedItem("y").getNodeValue());
                            	takeH = Integer.parseInt(takesArea.getAttributes().getNamedItem("h").getNodeValue());
                            	takeW = Integer.parseInt(takesArea.getAttributes().getNamedItem("w").getNodeValue());
                            	Rectangle takeArea = new Rectangle(takeX, takeY, takeW, takeH);
                                takeObjects.add(new TakeObject(name, takeN, takeArea));
                        	}
                        
                            numTakes++;
                        }
                    }
                }

                else if("parts".equals(sub.getNodeName())){ 
                	
                    NodeList partsNodes = sub.getChildNodes();
                    int partX = 0;
                    int partY = 0;
                    int partH = 0;
                    int partW = 0;
                    
                    for(int k = 0; k < partsNodes.getLength(); k++){
                        Node partNode = partsNodes.item(k);

                        if("part".equals(partNode.getNodeName())){

                        	NodeList partNodeList = partNode.getChildNodes();
                        	String roleName = partNode.getAttributes().getNamedItem("name").getNodeValue();
                            int level = Integer.parseInt(partNode.getAttributes().getNamedItem("level").getNodeValue());

                            for(int a = 0; a < partNodeList.getLength(); a++) {
                            	
                            	if("area".equals(partNodeList.item(a).getNodeName())) {
                            		
                            		Node areaNode = partNodeList.item(a); 
                            		partX = Integer.parseInt(areaNode.getAttributes().getNamedItem("x").getNodeValue());
                            		partY = Integer.parseInt(areaNode.getAttributes().getNamedItem("y").getNodeValue());
                            		partH = Integer.parseInt(areaNode.getAttributes().getNamedItem("h").getNodeValue());
                            		partW = Integer.parseInt(areaNode.getAttributes().getNamedItem("w").getNodeValue());
                            		Rectangle partRectangle = new Rectangle(partX, partY, partH, partW);
                            		ExtraPartObject extraPart = new ExtraPartObject(name, roleName, level, partRectangle);
                            		extraPartList.add(extraPart);
                                    roles.add(new Role(roleName, level, false, partRectangle));
                            	}
                            }
                        }     
                    }
                }
            }
            
            Rectangle setRectangle = new Rectangle(roomX, roomY, roomH, roomW);
            Set newSet = new Set(name, numTakes, roles, neighbors, setRectangle, takeObjects, extraPartList);
            newSet.setTakeObjects(takeObjects);
            rooms.add(newSet);
            roomObjects.put(name, newSet);
        }

        ArrayList<String> neighbors = new ArrayList<>();
        Node trailerNode = trailerRoom.item(0);
        NodeList trailerChildren = trailerNode.getChildNodes();
        NodeList trailerNeighbors = trailerChildren.item(1).getChildNodes();
        String trailerName = "trailer";
        for(int i = 0; i < trailerNeighbors.getLength(); i++){
        	
            Node tNeighbor = trailerNeighbors.item(i);
            
            if("neighbor".equals(tNeighbor.getNodeName())){
            	
                neighbors.add(tNeighbor.getAttributes().getNamedItem("name").getNodeValue());
            }
        }
        
        Node trailerArea = trailerChildren.item(3);
        int trailerX = 0, trailerY = 0, trailerH = 0, trailerW = 0;
        trailerX = Integer.parseInt(trailerArea.getAttributes().getNamedItem("x").getNodeValue());
        trailerY = Integer.parseInt(trailerArea.getAttributes().getNamedItem("y").getNodeValue());
    	trailerH = Integer.parseInt(trailerArea.getAttributes().getNamedItem("h").getNodeValue());
    	trailerW = Integer.parseInt(trailerArea.getAttributes().getNamedItem("w").getNodeValue());
   
        Rectangle rectangle = new Rectangle(trailerX, trailerY, trailerH, trailerW);
        Set newSet = new Set(trailerName, neighbors, rectangle);
        rooms.add(newSet);
        roomObjects.put("trailer", newSet);

        ArrayList<String> officeNeighbors = new ArrayList<>();
        ArrayList<Upgrade> upgrades = new ArrayList<>();

        Node officeNode = officeRoom.item(0);
        NodeList officeChildren = officeNode.getChildNodes();
        int areaX = 0, areaY = 0, areaH = 0, areaW = 0;

        for(int i = 0; i < officeChildren.getLength(); i++){
            Node officeSub = officeChildren.item(i);

            if("neighbors".equals(officeSub.getNodeName())){
                NodeList officeNeighborNodes = officeSub.getChildNodes();

                for(int k = 0; k < officeNeighborNodes.getLength(); k++){
                    Node neighborsNode = officeNeighborNodes.item(k);

                    if("neighbor".equals(neighborsNode.getNodeName())){
                    	
                        officeNeighbors.add(neighborsNode.getAttributes().getNamedItem("name").getNodeValue());
                    }
                }
            }
            
            if("area".equals(officeSub.getNodeName())) {
            	
            	Node areaList = officeChildren.item(i);
            	areaX = Integer.parseInt(areaList.getAttributes().getNamedItem("x").getNodeValue());
            	areaY = Integer.parseInt(areaList.getAttributes().getNamedItem("y").getNodeValue());
            	areaH = Integer.parseInt(areaList.getAttributes().getNamedItem("h").getNodeValue());
            	areaW = Integer.parseInt(areaList.getAttributes().getNamedItem("w").getNodeValue());
            }

            if("upgrades".equals(officeSub.getNodeName())){
                NodeList officeUpgradeNodes = officeSub.getChildNodes();
                
                for(int k = 0; k < officeUpgradeNodes.getLength(); k++){
                    Node upgradeNode = officeUpgradeNodes.item(k);

                    if("upgrade".equals(upgradeNode.getNodeName())){
                    	
                        int level = Integer.parseInt(upgradeNode.getAttributes().getNamedItem("level").getNodeValue());
                        String currency = upgradeNode.getAttributes().getNamedItem("currency").getNodeValue();
                        int amount = Integer.parseInt(upgradeNode.getAttributes().getNamedItem("amt").getNodeValue());
                        NodeList areaList = upgradeNode.getChildNodes();
                        int upgradeX = Integer.parseInt(areaList.item(1).getAttributes().getNamedItem("x").getNodeValue());
                        int upgradeY = Integer.parseInt(areaList.item(1).getAttributes().getNamedItem("y").getNodeValue());
                        int upgradeH = Integer.parseInt(areaList.item(1).getAttributes().getNamedItem("h").getNodeValue());
                        int upgradeW = Integer.parseInt(areaList.item(1).getAttributes().getNamedItem("w").getNodeValue());
                        Rectangle upgradeRectangle = new Rectangle(upgradeX, upgradeY, upgradeH, upgradeW);
                        upgrades.add(new Upgrade(level, currency, amount, upgradeRectangle));
                    }
                }
            }
        }

        Rectangle castingOfficeArea = new Rectangle(areaX, areaY, areaH, areaW);
        CastingOffice office = new CastingOffice(officeNeighbors, upgrades, castingOfficeArea);
        rooms.add(office);

        return rooms;
    }

    public ArrayList<Scene> readCardsData(Document d){ 
    	
        Element root = d.getDocumentElement(); 
        ArrayList<Scene> scenes = new ArrayList<>(); 
        NodeList sceneNodes = root.getElementsByTagName("card");

        for(int i = 0; i < sceneNodes.getLength(); i++) {

            Node sceneNode = sceneNodes.item(i);
            Element element = (Element) sceneNode;
            String name = sceneNode.getAttributes().getNamedItem("name").getNodeValue();
            String cardImage = sceneNode.getAttributes().getNamedItem("img").getNodeValue();
            int budget = Integer.parseInt(sceneNode.getAttributes().getNamedItem("budget").getNodeValue());
            int sceneNumber = Integer.parseInt(((Element) element.getElementsByTagName("scene").item(0)).getAttribute("number"));
            ArrayList<PartObject> onCardRoles = new ArrayList<PartObject>();
            
            ArrayList<Role> sceneRoles = new ArrayList<>(); 
            NodeList children = sceneNode.getChildNodes(); 
 
            for (int j = 0; j < children.getLength(); j++) { 
            	
            	Node sub = children.item(j);
            	
            	if("part".equals(sub.getNodeName())) {
            		
	            	Element currentElement = (Element) sub;
	                String roleName = sub.getAttributes().getNamedItem("name").getNodeValue();
	                int level = Integer.parseInt(sub.getAttributes().getNamedItem("level").getNodeValue());
	                NodeList subChildren = sub.getChildNodes();
	                
	                for(int k = 0; k < subChildren.getLength(); k++) {
	                	
	                	Node currentNode = subChildren.item(k);
	                	
	                	if("area".equals(currentNode.getNodeName())) {
	                		
	                		NodeList areaOfPart = currentElement.getElementsByTagName("area");
	                        Node currentNodeArea = areaOfPart.item(0);
	                  
	                        int partX = Integer.parseInt(currentNodeArea.getAttributes().getNamedItem("x").getNodeValue());
	                        int partY = Integer.parseInt(currentNodeArea.getAttributes().getNamedItem("y").getNodeValue());
	                        int partH = Integer.parseInt(currentNodeArea.getAttributes().getNamedItem("h").getNodeValue());
	                        int partW = Integer.parseInt(currentNodeArea.getAttributes().getNamedItem("w").getNodeValue());
	                        Rectangle rolePosition = new Rectangle(partX, partY, partW, partH);
	                        onCardRoles.add(new PartObject(roleName, level, rolePosition));
                            sceneRoles.add(new Role(roleName, level, true, rolePosition));
	                	}
	                }
            	}
            }
       
           Scene scene = new Scene(name, budget, sceneRoles, cardImage, sceneNumber, onCardRoles);
           scenes.add(scene);
           cards.add(scene);
        }

        return scenes;
    }
    
    public HashMap<String, Room> getRoomObjects(){
    	
    	return roomObjects;
    }
    
    public ArrayList<Scene> getCards(){
    	
    	return cards;
    } 
}
