use std::fs::File;
use std::io::{BufRead, BufReader, Result};

fn main() -> Result<()> {

    let f = File::open("01-18-input")?;

    let mut sum = 0;

    for line in BufReader::new(f).lines() {
        sum += line?.parse::<i32>().unwrap();
        // println!("{}", line?);
    }
    println!("{}", sum);
    Ok(())    
}

