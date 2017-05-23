import com.profitbricks.sdk.model.*
import static com.profitbricks.sdk.Commands.*

class SDKExample {
    final static void main(final String[] args) {
        
        //Creating a DataCenter
        println "Creating a DataCenter"
        
        DataCenter dc = new DataCenter(
            name: "example DC",
            location: 'de/fkb',
            description: 'desc'
        ).create() as DataCenter
        
        println "DataCenter Ready"
        // listing DataCenters
        println "Listing DataCenters"
        println dc.all
        

        //Add a lan
        println "Creating a public Lan"
        LAN lan = new LAN(
            dataCenter: dc,
            name: "public lan",
            _public: true
        ).create()
        
        println "Public Lan Ready"
        
        //Add a server
        println "Creating a Server"
        Server server = new Server(
            dataCenter: dc,
            name: "Example server",
            cores: 1,
            ram: 1024
        ).create()
        
        println "Server Ready"
        
        //reading Server
        println "Read server"
        println server.read()
        
        //Adding NIC to the example Server
        println "Adding NIC to server"
        NIC nic = new NIC(
            server: server,
            lan: lan,
            name: "example nic"
        ).create()
        println "NIC Ready"
        
        //Find a linux image to attach to volume
        println "Searching for a linux image"
        Image image = new Image()
        image = image.all.collect{image.read(it) as Image}.findAll{
            it._public &&
            it.location == dc.location &&
            it.licenceType =~ /(?i)linux/ &&
            it.imageType =~ /(?i)hdd/
        }.first()
        
        println "Linux Image found: $image"
        
        //Create a volume
        println "Creating a Volume"
        Volume volume = new Volume(
            dataCenter: dc,
            name: "os volume",
            size: 4,
            image: image.id,
            type: "HDD",
            imagePassword: "test1234"
            
        ).create()
        println "Volume Ready"
        
        
        //Attach the os Volume to the Example server
        println "Attaching the os Volume to the Example Server"
        attach(server, volume)
        println "Volume Attached"
        
        //List attached volumes
        println "Listing attached volumes"
        println attachedVolumes(server)

        //Example cleaning
        println "Started cleaning"
        def _dc = dc.read(dc.id) as DataCenter
        _dc.delete()
        println "Cleaning Complete"
    }
}

