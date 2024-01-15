//
//  WeatherManager.swift
//  MyWeatherApp
//
//  Created by Andrii Melnyk on 1/13/24.
//

import Foundation
import CoreLocation



let myApiKey = "9e1e5b532eb06a26403bb780680a3710"

let weatherUrl = ""

func createWeatherUrl( withString city:String){
    let url = "https://api.openweathermap.org/data/2.5/forecast?q=\(city)&appid=\(myApiKey)&units=metric&cnt=4"
//    let url = "api.openweathermap.org/data/2.5/forecast?lat=37.7657814&lon=-122.4075538&appid=9e1e5b532eb06a26403bb780680a3710"
    performRequest(with: url)
}

func fetchTheWeatherForMe( lat:Double, lon:Double){
    
    let url = "https://api.openweathermap.org/data/2.5/forecast?lat=\(lat)&lon=\(lon)&appid=9e1e5b532eb06a26403bb780680a3710&cnt=4"
//    let url = "api.openweathermap.org/data/2.5/forecast?lat=\(lat)&lon=\(lon)&appid=9e1e5b532eb06a26403bb780680a3710"
    print(url)
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
            print(String(data: safeData, encoding: .utf8) as Any)
            parseJSON(data: safeData)
        }
        task.resume()
    }
}

func parseJSON(data: Data) {
    var result: Response?
    let decoder = JSONDecoder()
    
    do {
        result = try decoder.decode(Response.self, from: data)
    } catch{
        print(error)
    }
    
    guard let json = result else {
        return
    }
    
//    let count = 1...8
//    
//    for number in count {
//        print("Temperature:\(json.list[number].main.temp)")
//    }
//        print("Feels like:\(json.main.feels_like)")
//        print("Today min temperature\(json.main.temp_min)")
//        print("Today max temperature:\(json.main.temp_max)")
//        print("Pressure\(json.main.pressure)")
//        print("Humidity\(json.main.humidity)")
}
