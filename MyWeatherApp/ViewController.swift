//
//  ViewController.swift
//  MyWeatherApp
//
//  Created by Andrii Melnyk on 1/12/24.
//

import UIKit
import CoreLocation

class ViewController: UIViewController, CLLocationManagerDelegate {

    var myLocation = CLLocationManager()
   
    var JSPNContent = [""]
    let myApiKey = "9e1e5b532eb06a26403bb780680a3710"
    let cityName = "Paris"
    let weatherUrl = ""
    //https://api.openweathermap.org/data/2.5/forecast?q=&appid=&units=metric#
    override func viewDidLoad() {
        
        myLocation.delegate = self
        createWeatherUrl()
        performRequest(with: weatherUrl)
//        myLocation.requestAlwaysAuthorization()
//        myLocation.requestWhenInUseAuthorization()
//        myLocation.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
//        myLocation.startUpdatingLocation()
//  
//        print(myLocation.location!.coordinate.latitude, myLocation.location!.coordinate.longitude)
        
        
        
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }


//    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
//        let locValue:CLLocationCoordinate2D = manager.location!.coordinate
//        print("locations = \(locValue.latitude) \(locValue.longitude)")
//    }
    
    func createWeatherUrl(){
        let url = "https://api.openweathermap.org/data/2.5/forecast?q=\(cityName)&appid=\(myApiKey)&units=metric"
        performRequest(with: url)
    }
    
    func performRequest(with urlString: String) {
        
        if let url = URL(string: urlString) {
            
            let session = URLSession(configuration: .default)
            
            let task = session.dataTask(with: url) { data, response, error in
                guard let safeData = data, error == nil  else {
                    print("Oops something went wrong \(error!)")
                    return
                }
               // print(String(data: safeData, encoding: .utf8))
                self.parseJSON(data: safeData)
            }
            task.resume()
        }
    }
    
    func parseJSON(data: Data) {
        var result: Response?
        let decoder = JSONDecoder()
        
        do {
            result = try JSONDecoder().decode(Response.self, from: data)
        } catch{
            print(error)
        }
        
        guard let json = result else {
            return
        }
        
        let count = 1...8
        
        for number in count {
            print("Temperature:\(json.list[number].main.temp)")
        }
//        print("Feels like:\(json.main.feels_like)")
//        print("Today min temperature\(json.main.temp_min)")
//        print("Today max temperature:\(json.main.temp_max)")
//        print("Pressure\(json.main.pressure)")
//        print("Humidity\(json.main.humidity)")
    }
}

struct Response : Decodable {
    
    var list: [List]
    //let weather: Double
}

struct Main: Codable {
    let temp : Double
    let feels_like: Double
    let temp_min: Double
    let temp_max: Double
    let pressure: Double
    let humidity: Double
}

struct List:Decodable {
    let main: Main
}
