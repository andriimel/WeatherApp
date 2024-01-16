//
//  ViewController.swift
//  MyWeatherApp
//
//  Created by Andrii Melnyk on 1/12/24.
//

import UIKit

import CoreLocation

class WeatherViewController: UIViewController {
    
    var weatherArray = ["1","2","3","4","5"]
    
    
    @IBOutlet weak var tempLabel: UILabel!
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var cityLabel: UILabel!
    @IBOutlet weak var descriptLabel: UILabel!
    @IBOutlet weak var dayLabel: UILabel!
    @IBOutlet weak var weatherTableView: UITableView!
    @IBOutlet weak var searchTextField: UITextField!
    
    var weatherManager = WeatherManager()
    var myLocation = CLLocationManager()
    
    override func viewDidLoad() {
        
        myLocation.delegate = self
        myLocation.requestAlwaysAuthorization()
        myLocation.requestWhenInUseAuthorization()
        myLocation.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
        myLocation.startUpdatingLocation()
        myLocation.requestLocation()
        
        setDay()
        
        weatherManager.delegate = self
        weatherTableView.delegate = self
        weatherTableView.dataSource = self
        searchTextField.delegate = self
        
        super.viewDidLoad()
    }
    
    // MARK: - Set portrait orientation method
    override public var supportedInterfaceOrientations: UIInterfaceOrientationMask {
        return .portrait
    }
    // MARK: - Set curremt day method
    func setDay(){
        
        let date = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "EEEE"
        let dayInWeek = dateFormatter.string(from: date)
        dayLabel.text = dayInWeek
        print (dayInWeek)
    }
}
// MARK: - MyLocation methods

extension WeatherViewController:  CLLocationManagerDelegate {
    @IBAction func myLocationButtonPressed(_ sender: UIButton) {
        
        print("MyLocationButton pressed !!! ")
        
        DispatchQueue.main.async {
            self.myLocation.requestLocation()
            self.weatherManager.fetchTheWeatherForMe(lat: self.myLocation.location!.coordinate.latitude, lon: self.myLocation.location!.coordinate.longitude)
            
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
        if let location = locations.last {
            myLocation.stopUpdatingLocation()
            let lat = location.coordinate.latitude
            let lon = location.coordinate.longitude
            weatherManager.fetchTheWeatherForMe(lat: lat, lon: lon)
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("We have some problems with Internet connection \(error)")
    }
}

// MARK: - UITextField delegates method
extension WeatherViewController: UITextFieldDelegate {
    @IBAction func searchButtonPressed(_ sender: UIButton) {
        
        searchTextField.endEditing(true)
        weatherTableView.reloadData()
        print("SearchButtonPressed")
    }
    
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        
        searchTextField.endEditing(true)
        
        return true
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        
        if let city = searchTextField.text {
            weatherManager.createWeatherUrl(withString: city)
        }
        
        searchTextField.text = ""
    }
    
}
// MARK: - UITableView dataSource method
extension WeatherViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return weatherArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
        
        cell.textLabel?.text = weatherArray[indexPath.row]
        cell.textLabel?.textColor = .systemBlue
        cell.textLabel?.font = UIFont(name: "System Black", size: 15.0)
        
        // print("Cell is created, temp is \(tempString)")
        return cell
    }
    
}

// MARK: - Weather manager delegate method

extension WeatherViewController: WeatherManagerDelegate {
    func didUpdateWeather(_ weatherManager: WeatherManager, weather: WeatherModel) {
        DispatchQueue.main.async {
            
            self.tempLabel.text = String(format:"%.0f",weather.temperature)
            self.imageView.image = UIImage(systemName: weather.conditionName)
            self.cityLabel.text = weather.cityName
            self.descriptLabel.text = weather.description
            
            self.weatherArray[0] = String(format: "Current temperature:   %.0f °",weather.temperature)
            self.weatherArray[1] = String(format: "Today max temperature:  %.0f °",weather.maxTemperature)
            self.weatherArray[2] = String(format: "Today min temperature:   %.0f °",weather.minTemperature)
            self.weatherArray[3] = String(format: "Pressure :    %.0f",weather.pressure)
            self.weatherArray[4] = String(format: "Humidity :     %.0f", weather.humidity)
            
            self.weatherTableView.reloadData()
            
        }
    }
    
    func didFailWithError(error: Error) {
        print(error)
    }
}
