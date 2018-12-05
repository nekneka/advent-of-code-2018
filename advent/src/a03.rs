use std::fs::File;
use std::io::{BufRead, BufReader};
use regex::Regex;


pub fn main() {
    let _ = fill_fabric();
}

fn fill_fabric() { 
    let re = Regex::new(r"@\s([\d]+),([\d]+):\s([\d]+)x([\d]+)$").unwrap();

    let f = File::open("src/03-18-input").unwrap();
    let mut fabric: [[u32; 1200]; 1200] = [[0; 1200]; 1200];

    let mut count = 0;
    for line in BufReader::new(f).lines() {
        let l = &line.unwrap();
        for cap in re.captures_iter(l) {
            let start_x = *(&cap[1].parse::<u32>().unwrap()) as usize;
	    let end_x = start_x + &cap[3].parse::<usize>().unwrap();
            let start_y = *(&cap[2].parse::<u32>().unwrap()) as usize;
            let end_y = start_y + &cap[4].parse::<usize>().unwrap();

	    for x in start_x..end_x {
		for y in start_y..end_y {
		    if fabric[x][y] == 0 { fabric[x][y] = 1; }
		    else if fabric[x][y] == 1 { fabric[x][y] = 2; count += 1; }
		}
	    }
        }
    }

    println!("count: {}", count);
}

