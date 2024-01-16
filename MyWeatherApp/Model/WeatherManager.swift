//
//  WeatherManager.swift
//  MyWeatherApp
//
//  Created by Andrii Melnyk on 1/13/24.
//

import Foundation
import CoreLocation

protocol WeatherManagerDelegate {
   
    func didUpdateWeather(_ weatherManager:WeatherManager, weather: WeatherModel)
    func didFailWithError (error: Error)
    
}

struct WeatherManager {
    
    
    let myApiKey = "9e1e5b532eb06a26403bb780680a3710"
    
    var delegate: WeatherManagerDelegate?
  
    func createWeatherUrl( withString city:String){
        let url = "https://api.openweathermap.org/data/2.5/weather?q=\(city)&appid=\(myApiKey)&units=metric&cnt=4"
       
        print(url)
        performRequest(with: url)
    }
    
    func fetchTheWeatherForMe( lat:Double, lon:Double){
        
        let url = "https://api.openweathermap.org/data/2.5/weather?lat=\(lat)&lon=\(lon)&appid=9e1e5b532eb06a26403bb780680a3710&units=metric"
       
        print(url)
        performRequest(with: url)
    }
    
    func performRequest(with urlString: String) {
        
        if let url = URL(string: urlString) {
            
            let session = URLSession(configuration: .default)
            
            let task = session.dataTask(with: url) { data, response, error in
                guard let safeData = data, error == nil  else {
                    delegate?.didFailWithError(error: error!)
                    return
                }
                if let weather = self.parseJSON(data: safeData) {
                    delegate?.didUpdateWeather(self, weather: weather)
                   
                }
            }
            task.resume()
        }
    }
    
    func parseJSON(data: Data)-> WeatherModel? {
        
        // var result: Response?
        let decoder = JSONDecoder()
        
        do {
            let  result = try decoder.decode(Response.self, from: data)
            
            let temp = result.main.temp
            let maxTemp = result.main.temp_max
            let minTemp = result.main.temp_min
            let feelLike = result.main.feels_like
            let press = result.main.pressure
            let hum = result.main.humidity
            let conId = result.weather[0].id
            let city = result.name
            let descript = result.weather[0].description
            
            let weather = WeatherModel(temperature: temp, feelsLike: feelLike, minTemperature: minTemp, maxTemperature: maxTemp, pressure: press, humidity: hum, conditionID: conId, description: descript, cityName: city)
          
            print(weather)
            return weather
            
        } catch{
            delegate?.didFailWithError(error: error)
            return nil
        }
        
    }
}
