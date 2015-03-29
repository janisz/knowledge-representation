initially [-roomClosed, -hostelClosed, inHostel, -hasCard]
(Janek, closesDoor) causes [hostelClosed]
typically [-hasCard] triggers (Janek, takesCard)
typically (Janek, leaves) invokes (Janek, locksTheDoor)
(Janek, takesCard) causes [hasCard]
(Janek, leaves) causes [-inHostel]
(Janek, comebacks) causes [inHostel] after 10 if [hasCard]
