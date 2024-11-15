// Select the 'wander' database if not already selected
const db = connect('localhost/wander');

// Insert data into the 'users' collection
db.users.insertMany([
  {
    _id: ObjectId(),
    name: "",
    email: "",
    passwordHash: "",
    role: "", // Possible values: "tourist", "provider"
    preferences: [],
    location: "",
    createdAt: new Date(),
    bookings: []
  }
]);

// Insert data into the 'experiences' collection
db.experiences.insertMany([
  {
    _id: ObjectId(),
    title: "",
    description: "",
    location: "",
    hostId: ObjectId(),
    price: 0,
    availabilityDates: [],
    tags: [],
    rating: 0,
    capacity: 0,
    createdAt: new Date()
  }
]);

// Insert data into the 'bookings' collection
db.bookings.insertMany([
  {
    _id: ObjectId(),
    experienceId: ObjectId(),
    userId: ObjectId(),
    status: "pending", // Valores posibles: "pending", "confirmed", "canceled"
    bookingDate: new Date(),
    totalPrice: 0,
    participants: 0,
    paymentStatus: "pending", // Possible values: "paid", "pending"
    createdAt: new Date()
  }
]);

// Insert data into the 'reviews' collection
db.reviews.insertMany([
  {
    _id: ObjectId(),
    experienceId: ObjectId(),
    userId: ObjectId(),
    rating: 0,
    comment: "",
    date: new Date()
  }
]);

// Insert data into the 'needs' collection
db.needs.insertMany([
  {
    _id: ObjectId(),
    experienceId: ObjectId(),
    type: "",
    quantity: 0,
    status: "pending" // Possible values: "pending", "covered"
  }
]);

// Insert data into the 'transactions' collection
db.transactions.insertMany([
  {
    _id: ObjectId(),
    userId: ObjectId(),
    amount: 0,
    transactionType: "payment", // Possible values: "payment", "donation"
    relatedId: ObjectId(),
    status: "completed", // Possible values: "completed", "failed"
    date: new Date()
  }
]);

// Insert data into the 'notifications' collection
db.notifications.insertMany([
  {
    _id: ObjectId(),
    userId: ObjectId(),
    type: "",
    message: "",
    isRead: false,
    createdAt: new Date()
  }
]);
