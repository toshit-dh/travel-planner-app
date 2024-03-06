const multer = require('multer');
const { v4 } = require('uuid');
const MIME_TYPE_MAP = {
    'image/png': 'png',
    'image/jpeg': 'jpeg',
    'image/jpg': 'jpg',
    
}
const file = multer({
    limits: 500000000,
    storage: multer.diskStorage({
        destination: (req, file, cb) => {
            cb(null, 'data/photos');
        },
        filename: (req, file, cb) => {
            const {user} = req.user
            const ext = MIME_TYPE_MAP[file.mimetype];
            cb(null, user +  v4() + '.' + ext);
        },
    }),
    fileFilter: (req, file, cb) => {
        const isValid =!!MIME_TYPE_MAP[file.mimetype];
        let error = isValid ? null : new Error('Invalid File Type');
        cb(error, isValid);
    },
});

module.exports = file