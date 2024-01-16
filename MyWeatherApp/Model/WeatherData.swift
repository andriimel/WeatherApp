//
//  WeatherData.swift
//  MyWeatherApp
//
//  Created by Andrii Melnyk on 1/13/24.
//

import Foundation

struct Response : Decodable {
    
    let name: String
    let main: Main
    let weather:[Weather]
}

struct Main: Codable {
    let temp : Double
    let feels_like: Double
    let temp_min: Double
    let temp_max: Double
    let pressure: Double
    let humidity: Double
}

struct Weather:Decodable {
    let description:String
    let id: Int
}

