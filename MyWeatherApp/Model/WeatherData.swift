//
//  WeatherData.swift
//  MyWeatherApp
//
//  Created by Andrii Melnyk on 1/13/24.
//

import Foundation

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

