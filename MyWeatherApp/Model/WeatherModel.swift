//
//  WeatherModel.swift
//  MyWeatherApp
//
//  Created by Andrii Melnyk on 1/14/24.
//

import Foundation

struct WeatherModel {
    
    var temperature: Double
    var feelsLike: Double
    var minTemperature: Double
    var maxTemperature: Double
    var pressure: Double
    var humidity: Double
    
    let conditionID: Int
    let description: String
    let cityName:String
    
    
    var temperatureString:String {
        return String(format: "%.1f",temperature)
    }
    
    var conditionName:String {
        switch conditionID {
                case 200...232:
                    return "cloud.bolt"
                case 300...321:
            
                    return "cloud.drizzle"
                case 500...531:
            
                    return "cloud.rain"
                case 600...622:

                    return "cloud.snow"
                case 701...781:
           
                    return "cloud.fog"
                case 800:

                    return "sun.max"
                case 801...804:

                    return "cloud.bolt"
                default:
                    return "cloud"
                }
    }
}



