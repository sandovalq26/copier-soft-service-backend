# Lista de Tipos de archivo
GET: /api/v1/file-types

/*
- Debe ser protegido
- Del token debe recuperar el user id y luego recuperar el id de la empresa asociada por el campo user_id de la tabla hk_user 
- Luego debe listar todos los tipos de archivo de la empresa asociada 
*/

// Response 200 OK
[
    {
        "id": 1,
        "companyId": 1,
        "name": "string",
        "description": "string",
        "status": true,
        "createdAt": "2026-05-15 03:33:09",
        "updatedAt": "2026-05-15 03:33:09"
    }
]

// Response 401 Unauthorized
{
    "code": "UNAUTHORIZED",
    "message": "No autorizado."
}

// Response 403 Forbidden
{
    "code": "FORBIDDEN",
    "message": "No autorizado."
}

# Obtiene un tipo de archivo por id
GET: /api/v1/file-type/{id} 

/*
- Debe ser protegido
- Del token debe recuperar el user id y luego recuperar el id de la empresa asociada por el campo user_id de la tabla hk_user 
- Luego debe validar que el id del tipo de archivo sea de la empresa asociada
- En caso de que no sea de la empresa asociada debe retornar un error 403
- En caso de que el tipo de archivo no exista debe retornar un error 404
*/

// Response 200 OK
{
    "id": 1,
    "companyId": 1,
    "name": "string",
    "description": "string",
    "status": true,
    "createdAt": "2026-05-15 03:33:09",
    "updatedAt": "2026-05-15 03:33:09"
}

// Response 400 Bad Request
{
    "code": "VALIDATION_ERROR",
    "message": "Error al validar los datos."
}

// Response 401 Unauthorized
{
    "code": "UNAUTHORIZED",
    "message": "No autorizado."
}

// Response 403 Forbidden
{
    "code": "FORBIDDEN",
    "message": "No autorizado."
}

// Response 404 Not Found
{
    "code": "NOT_FOUND",
    "message": "No se encontró el tipo de archivo."
}

# Crea un tipo de archivo
POST: /api/v1/file-type

/*
- Debe ser protegido
- Del token debe recuperar el user id y luego recuperar el id de la empresa asociada por el campo user_id de la tabla hk_user 
- Usar el id de empresa recuperado para crear el tipo de archivo
- por defecto el estado sera 1
- el created_by toma el valor del user id recuperado de la tabla hk_user
- la fecha de creacion toma el por defecto de la bd
- la fecha de actualizacion toma el por defecto de la bd
- el name debe ser oblicagotio y maximo 100 caracteres
- el description debe ser opcional y maximo 255 caracteres  
- en caso de cualquier error de validación de campos debe retornar un error 400 Bad Request
*/

// Request Body
{
    "name": "string",
    "description": "string"
}   

// Response 201 Created
{
    "data": 1,
    "message": "Tipo de archivo creado correctamente."
}

// Response 400 Bad Request
{
    "code": "VALIDATION_ERROR",
    "message": "Error al validar los datos.",
    "errors": {
        "field": [
            "message"
        ]
    }
}

// Response 401 Unauthorized
{
    "code": "UNAUTHORIZED",
    "message": "No autorizado."
}

// Response 403 Forbidden
{
    "code": "FORBIDDEN",
    "message": "No autorizado."
}

// Response 404 Not Found
{
    "code": "NOT_FOUND",
    "message": "No se encontró el tipo de archivo."
}


# Actualiza un tipo de archivo por id
PUT: /api/v1/file-type/{id}

/*
- Debe ser protegido
- Del token debe recuperar el user id y luego recuperar el id de la empresa asociada por el campo user_id de la tabla hk_user 
- Validar que el id del tipo de archivo pertenezca a la empresa asociada
- En caso de que el tipo de archivo no exista debe retornar un error 404
- En caso de que el tipo de archivo pertenezca a otra empresa debe retornar un error 403
*/

// Request Body
{
    "name": "string",
    "description": "string"
}

// Response 200 OK
{
    "data": 1,
    "message": "Tipo de archivo actualizado correctamente."
}

// Response 400 Bad Request
{
    "code": "VALIDATION_ERROR",
    "message": "Error al validar los datos.",
    "errors": {
        "field": [
            "message"
        ]
    }
}

// Response 401 Unauthorized
{
    "code": "UNAUTHORIZED",
    "message": "No autorizado."
}

// Response 403 Forbidden
{
    "code": "FORBIDDEN",
    "message": "No autorizado."
}

// Response 404 Not Found
{
    "code": "NOT_FOUND",
    "message": "No se encontró el tipo de archivo."
}

# Actualiza el estado de un tipo de archivo por id
PUT: /api/v1/file-type/{id}/status

/*
- Debe ser protegido
- Del token debe recuperar el user id y luego recuperar el id de la empresa asociada por el campo user_id de la tabla hk_user 
- Validar que el id del tipo de archivo pertenezca a la empresa asociada
- En caso de que el tipo de archivo no exista debe retornar un error 404
- En caso de que el tipo de archivo pertenezca a otra empresa debe retornar un error 403
*/

// Request Body
{
    "status": true
}

// Response 200 OK
{
    "data": 1,
    "message": "Tipo de archivo actualizado correctamente."
}

// Response 400 Bad Request
{
    "code": "VALIDATION_ERROR",
    "message": "Error al validar los datos.",
    "errors": {
        "field": [
            "message"
        ]
    }
}

// Response 401 Unauthorized
{
    "code": "UNAUTHORIZED",
    "message": "No autorizado."
}

// Response 403 Forbidden
{
    "code": "FORBIDDEN",
    "message": "No autorizado."
}

// Response 404 Not Found
{
    "code": "NOT_FOUND",
    "message": "No se encontró el tipo de archivo."
}
