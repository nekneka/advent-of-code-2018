use std::fs::File;
    
use std::io::{BufRead, BufReader};
use std::collections::HashMap;

fn main() {
    let _ = checksum();
}

fn checksum() {
    let mut num_of_twos = 0;
    let mut num_of_threes = 0;

    let f = File::open("02-18-input").unwrap();
    for line in BufReader::new(f).lines() {
        let mut dic_count = HashMap::new();
        let mut is_two = 0;
        let mut is_three = 0;

        for ch in line.unwrap().chars() {

            let counter = dic_count.entry(ch).or_insert(0);

            *counter += 1;
            if *counter == 2 {
                is_two += 1;
            }
            if *counter == 3 {
                is_two -= 1;
                is_three += 1;
            }
            if *counter == 4 {
                is_three -= 1;
            }
        }
        
        if is_two > 0 { num_of_twos += 1; }
        if is_three > 0 { num_of_threes += 1; }

    }

    println!("Checksum: {}", num_of_twos*num_of_threes);
                
}


